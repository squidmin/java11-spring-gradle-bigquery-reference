package org.squidmin.java.spring.gradle.bigquery.service;

import com.google.cloud.bigquery.InsertAllRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.squidmin.java.spring.gradle.bigquery.CliConfig;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryUtil;
import org.squidmin.java.spring.gradle.bigquery.util.LoggerUtil;

import java.util.List;

@Slf4j
public class BigQueryAdminClientTest extends CliConfig {

    @Test
    public void echoRunConfig() {
        LoggerUtil.logBqProperties(runEnvironment, LoggerUtil.ProfileOption.DEFAULT);
    }

    @Test
    void listDatasets() {
        bigQueryAdminClient.listDatasets();
    }

    @Test
    void createDataset() {
        bigQueryAdminClient.createDataset(GCP_DEFAULT_USER_DATASET);
    }

    @Test
    void deleteDataset() {
        bigQueryAdminClient.deleteDataset(GCP_DEFAULT_USER_PROJECT_ID, GCP_DEFAULT_USER_DATASET);
    }

    @Test
    void createTableWithDefaultSchema() {
        LoggerUtil.logBqProperties(runEnvironment, LoggerUtil.ProfileOption.ACTIVE);
        Assertions.assertTrue(
            bigQueryAdminClient.createTable(GCP_DEFAULT_USER_DATASET, GCP_DEFAULT_USER_TABLE)
        );
    }

    @Test
    void createTableWithCustomSchema() {
        LoggerUtil.logBqProperties(runEnvironment, LoggerUtil.ProfileOption.ACTIVE);
        Assertions.assertTrue(
            bigQueryAdminClient.createTable(
                GCP_DEFAULT_USER_DATASET,
                GCP_DEFAULT_USER_TABLE,
                BigQueryUtil.InlineSchemaTranslator.translate(schemaOverrideString, bigQueryConfig.getDataTypes())
            )
        );
    }

    @Test
    void deleteTable() {
        bigQueryAdminClient.deleteTable(
            GCP_DEFAULT_USER_PROJECT_ID,
            GCP_DEFAULT_USER_DATASET,
            GCP_DEFAULT_USER_TABLE
        );
    }

    @Test
    void insert() {
        List<InsertAllRequest.RowToInsert> rowsInserted = bigQueryAdminClient.insert(
            GCP_DEFAULT_USER_PROJECT_ID,
            GCP_DEFAULT_USER_DATASET,
            GCP_DEFAULT_USER_TABLE,
            BigQueryFunctionalTestFixture.DEFAULT_ROWS.get()
        );
        Assertions.assertTrue(0 < rowsInserted.size());
        rowsInserted.forEach(row -> Logger.log(String.valueOf(row), Logger.LogType.INFO));
    }

}
