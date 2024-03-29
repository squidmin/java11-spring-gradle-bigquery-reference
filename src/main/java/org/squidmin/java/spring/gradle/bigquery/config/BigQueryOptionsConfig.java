package org.squidmin.java.spring.gradle.bigquery.config;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;
import org.squidmin.java.spring.gradle.bigquery.util.LoggerUtil;
import org.squidmin.java.spring.gradle.bigquery.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@ComponentScan(basePackages = {
    "org.squidmin.java.spring.gradle.bigquery"
})
@Getter
@Slf4j
public class BigQueryOptionsConfig {

    private final String gcpDefaultUserProjectId;

    private final String gcpSaKeyPath = System.getProperty("GCP_SA_KEY_PATH");
    private final String gcpAdcAccessToken = System.getProperty("GCP_ADC_ACCESS_TOKEN");
    private final String gcpSaAccessToken = System.getProperty("GCP_SA_ACCESS_TOKEN");

    private final BigQuery bigQuery;

    public BigQueryOptionsConfig(
        @Value("${bigquery.application-default.project-id}") String gcpDefaultUserProjectId,
        BigQuery bigQuery) {

        this.gcpDefaultUserProjectId = gcpDefaultUserProjectId;
        this.bigQuery = bigQuery;

    }

    @Bean
    public BigQuery bigQuery() {
        Logger.log(String.format("BQ JDK: GCP_SA_KEY_PATH == %s", gcpSaKeyPath), Logger.LogType.CYAN);
        File credentialsPath = new File(gcpSaKeyPath);
        Logger.log(String.format("GCP_ADC_ACCESS_TOKEN == %s", gcpAdcAccessToken), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_SA_ACCESS_TOKEN == %s", gcpSaAccessToken), Logger.LogType.CYAN);

        BigQueryOptions.Builder bqOptionsBuilder = BigQueryOptions.newBuilder();
        bqOptionsBuilder.setProjectId(gcpDefaultUserProjectId).setLocation("us");
        GoogleCredentials credentials;
        boolean isBqJdkAuthenticatedUsingSaKeyFile;
        try (FileInputStream stream = new FileInputStream(credentialsPath)) {
            credentials = ServiceAccountCredentials.fromStream(stream);
            Logger.log("BQ JDK: SETTING SERVICE ACCOUNT CREDENTIALS (GOOGLE_APPLICATION_CREDENTIALS) TO BQ OPTIONS.", Logger.LogType.CYAN);
            bqOptionsBuilder.setCredentials(credentials);
            isBqJdkAuthenticatedUsingSaKeyFile = true;
        } catch (IOException e) {
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
            if (e.getMessage().contains("'type' value 'authorized_user' not recognized. Expecting 'service_account'")) {
                Logger.log("If you're trying to use Application Default Credentials (ADC), use the command:", Logger.LogType.ERROR);
                Logger.log("    gcloud auth application-default print-access-token", Logger.LogType.ERROR);
                Logger.log("to generate a GCP access token and set the output of the command to the \"GCP_ADC_ACCESS_TOKEN\" environment variable.", Logger.LogType.ERROR);
            }
            isBqJdkAuthenticatedUsingSaKeyFile = false;
        }

        LoggerUtil.logSaKeyFileAuth(isBqJdkAuthenticatedUsingSaKeyFile);

        if (!isBqJdkAuthenticatedUsingSaKeyFile && StringUtils.isNotEmpty(gcpAdcAccessToken)) {
            Logger.log("Authenticated successfully using Application Default Credentials (ADC) access token.", Logger.LogType.INFO);
            return bqOptionsBuilder.setCredentials(
                GoogleCredentials.newBuilder()
                    .setAccessToken(AccessToken.newBuilder().setTokenValue(gcpAdcAccessToken).build())
                    .build()
            ).build().getService();
        } else {
            Logger.log("Was not able to authenticate using Application Default Credentials (ADC) access token.", Logger.LogType.INFO);
            return bqOptionsBuilder.build().getService();
        }
    }

}
