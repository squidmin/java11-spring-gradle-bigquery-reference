package org.squidmin.java.spring.gradle.bigquery.config;

import com.google.cloud.bigquery.BigQuery;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClient;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryTimeUtil;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryUtil;
import org.squidmin.java.spring.gradle.bigquery.util.TemplateCompiler;

import java.io.IOException;

@Configuration
@ActiveProfiles("integration")
@Slf4j
public class IntegrationTestConfig {

    @Value("${bigquery.application-default.project-id}")
    private String gcpDefaultUserProjectId;

    @Value("${bigquery.application-default.dataset}")
    private String gcpDefaultUserDataset;

    @Value("${bigquery.application-default.table}")
    private String gcpDefaultUserTable;

    @Value("${bigquery.service-account.project-id}")
    private String gcpSaProjectId;

    @Value("${bigquery.service-account.dataset}")
    private String gcpSaDataset;

    @Value("${bigquery.service-account.table}")
    private String gcpSaTable;

    @Value("${bigquery.uri.queries}")
    private String queryUri;

    private BigQueryUtil bigQueryUtil;
    private final BigQueryTimeUtil bigQueryTimeUtilMock = Mockito.mock(BigQueryTimeUtil.class);

    private BigQueryConfig bigQueryConfig;
    private final BigQueryConfig bigQueryConfigMock = Mockito.mock(BigQueryConfig.class);

    private final BigQuery bigQueryMock = Mockito.mock(BigQuery.class);

    private BigQueryOptionsConfig bigQueryOptionsConfig;
    private final BigQueryOptionsConfig bigQueryOptionsConfigMock = Mockito.mock(BigQueryOptionsConfig.class);

    private BigQueryAdminClient bigQueryAdminClient;

    private TemplateCompiler templateCompiler;

    private final RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);

    @Bean
    public String gcpDefaultUserProjectId() {
        return gcpDefaultUserProjectId;
    }

    @Bean
    public String gcpDefaultUserDataset() {
        return gcpDefaultUserDataset;
    }

    @Bean
    public String gcpDefaultUserTable() {
        return gcpDefaultUserTable;
    }

    @Bean
    public String gcpSaProjectId() {
        return gcpSaProjectId;
    }

    @Bean
    public String gcpSaDataset() {
        return gcpSaDataset;
    }

    @Bean
    public String gcpSaTable() {
        return gcpSaTable;
    }

    @Bean
    public String queryUri() {
        return queryUri;
    }

    @Bean
    @Qualifier("restTemplateMock_integrationTest")
    public RestTemplate restTemplateMock() {
        return restTemplateMock;
    }

    @Bean
    public BigQueryUtil bigQueryUtil() {
        bigQueryUtil = new BigQueryUtil(templateCompiler);
        return bigQueryUtil;
    }

    @Bean
    @Qualifier("bigQueryConfig_integrationTest")
    public BigQueryConfig bigQueryConfig() throws IOException {
        bigQueryConfig = new BigQueryConfig(
            gcpDefaultUserProjectId,
            gcpDefaultUserDataset,
            gcpDefaultUserTable,
            gcpSaProjectId,
            gcpSaDataset,
            gcpSaTable,
            queryUri,
            BigQueryFunctionalTestFixture.validSchemaDefault(),
            BigQueryFunctionalTestFixture.validDataTypes(),
            BigQueryFunctionalTestFixture.validSelectFieldsDefault(),
            BigQueryFunctionalTestFixture.validWhereFieldsDefault(),
            BigQueryFunctionalTestFixture.validExclusions(),
            false,
            bigQueryOptionsConfig()
        );
        return bigQueryConfig;
    }

    @Bean
    @Qualifier("bigQueryConfigMock_integrationTest")
    public BigQueryConfig bigQueryConfigMock() {
        return Mockito.mock(BigQueryConfig.class);
    }

    @Bean
    @Qualifier("bigQuery_integrationTest")
    public BigQuery bigQuery() { return bigQueryOptionsConfig.getBigQuery(); }
    @Bean
    @Qualifier("bigQueryMock_integrationTest")
    public BigQuery bigQueryMock() {
        return bigQueryMock;
    }

    @Bean
    @Qualifier("bigQueryTimeUtilMock_integrationTest")
    public BigQueryTimeUtil bigQueryTimeUtilMock() {
        return bigQueryTimeUtilMock;
    }

    @Bean
    @Qualifier("bigQueryOptionsConfig_integrationTest")
    public BigQueryOptionsConfig bigQueryOptionsConfig() {
        bigQueryOptionsConfig = new BigQueryOptionsConfig(gcpDefaultUserProjectId(), bigQueryMock());
        return bigQueryOptionsConfig;
    }
    @Bean
    @Qualifier("bigQueryOptionsConfigMock_integrationTest")
    public BigQueryOptionsConfig bigQueryOptionsConfigMock() {
        return bigQueryOptionsConfigMock;
    }

    @Bean
    public BigQueryAdminClient bigQueryAdminClient() {
        bigQueryAdminClient = new BigQueryAdminClient(bigQueryConfigMock(), restTemplateMock());
        return bigQueryAdminClient;
    }

    @Bean
    public TemplateCompiler templateCompiler() {
        templateCompiler = new TemplateCompiler(bigQueryTimeUtilMock());
        return templateCompiler;
    }

}
