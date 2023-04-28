package org.squidmin.java.spring.gradle.bigquery;

import com.google.cloud.bigquery.Schema;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClient;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.ExampleSchema;
import org.squidmin.java.spring.gradle.bigquery.config.IntegrationTestConfig;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFixture;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryResourceMetadata;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryUtil;

@SpringBootTest(classes = { BigQueryAdminClient.class, IntegrationTestConfig.class })
@ActiveProfiles("integration")
@Slf4j
public class IntegrationTest {

    @Autowired
    protected BigQueryConfig bqConfig;

    protected BigQueryAdminClient bqAdminClient;

    protected String projectIdDefault, datasetNameDefault, tableNameDefault,
            projectIdOverride, datasetNameOverride, tableNameOverride, schemaOverride;
    protected ExampleSchema exampleSchemaDefault;
    protected Schema _schemaOverride;

    // The default values of configured BigQuery resource properties can be overridden by the values of CLI arguments.
    protected String PROJECT_ID, DATASET_NAME, TABLE_NAME;
    protected Schema SCHEMA;

    protected BigQueryResourceMetadata bqResourceMetadata = BigQueryResourceMetadata.builder().build();

    @BeforeEach
    public void before() {
        initialize();
        bqAdminClient = new BigQueryAdminClient(bqConfig);
        Logger.log(String.format("Active profile ID: %s", System.getProperty("profileId")), Logger.LogType.CYAN);
    }

    private void initialize() {
        initBqResourcePropertyDefaultValues();
        initBqResourcePropertyOverriddenValues();
        initBqResourcePropertyMetadata();
        initBqResourceActiveProperties();
    }

    private void initBqResourcePropertyDefaultValues() {
        projectIdDefault = bqConfig.getProjectId();
        datasetNameDefault = bqConfig.getDatasetName();
        tableNameDefault = bqConfig.getTableName();
        exampleSchemaDefault = bqConfig.getSchema();
        bqResourceMetadata = BigQueryResourceMetadata.builder()
            .projectIdDefault(bqConfig.getProjectId())
            .datasetNameDefault(bqConfig.getDatasetName())
            .tableNameDefault(bqConfig.getTableName())
            .schema(BigQueryUtil.translate(bqConfig.getSchema()))
            .build();
    }

    private void initBqResourcePropertyOverriddenValues() {
        projectIdOverride = System.getProperty(BigQueryFixture.CLI_ARG_KEYS.projectId.name());
        datasetNameOverride = System.getProperty(BigQueryFixture.CLI_ARG_KEYS.datasetName.name());
        tableNameOverride = System.getProperty(BigQueryFixture.CLI_ARG_KEYS.tableName.name());
        schemaOverride = System.getProperty(BigQueryFixture.CLI_ARG_KEYS.schema.name());
        if (null != schemaOverride) { _schemaOverride = BigQueryUtil.translate(schemaOverride); }
    }

    private void initBqResourcePropertyMetadata() {
        bqResourceMetadata.setProjectId(setBqResourceProperty(projectIdDefault, projectIdOverride));
        bqResourceMetadata.setDatasetName(setBqResourceProperty(datasetNameDefault, datasetNameOverride));
        bqResourceMetadata.setTableName(setBqResourceProperty(tableNameDefault, tableNameOverride));
        bqResourceMetadata.setSchema(null != schemaOverride ? _schemaOverride : BigQueryUtil.translate(exampleSchemaDefault));
    }

    private void initBqResourceActiveProperties() {
        PROJECT_ID = bqResourceMetadata.getProjectId();
        DATASET_NAME = bqResourceMetadata.getDatasetName();
        TABLE_NAME = bqResourceMetadata.getTableName();
        SCHEMA = bqResourceMetadata.getSchema();
    }

    private String setBqResourceProperty(String defaultValue, String overrideValue) {
        return null != overrideValue ? overrideValue : defaultValue;
    }

}