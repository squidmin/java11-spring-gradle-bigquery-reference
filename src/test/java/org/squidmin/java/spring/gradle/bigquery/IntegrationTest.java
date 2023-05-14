package org.squidmin.java.spring.gradle.bigquery;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.IntegrationTestConfig;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryIntegrationTestFixture;
import org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClient;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryUtil;
import org.squidmin.java.spring.gradle.bigquery.util.RunEnvironment;
import org.squidmin.java.spring.gradle.bigquery.util.StringUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BigQueryAdminClient.class, IntegrationTestConfig.class})
@ActiveProfiles("integration")
@Slf4j
public abstract class IntegrationTest {

    @Autowired
    protected BigQueryConfig bqConfig;

    protected BigQueryAdminClient bqAdminClient;

    @Autowired
    protected RestTemplate restTemplate;

    protected String
        gcpDefaultUserProjectIdDefault, gcpDefaultUserDatasetDefault, gcpDefaultUserTableDefault,
        gcpDefaultUserProjectIdCliOverride, gcpDefaultUserDatasetCliOverride, gcpDefaultUserTableCliOverride,
        gcpSaProjectIdDefault, gcpSaDatasetDefault, gcpSaTableDefault,
        gcpSaProjectIdCliOverride, gcpSaDatasetCliOverride, gcpSaTableCliOverride,
        schemaOverrideString;

    protected SchemaDefault schemaDefault;
    protected com.google.cloud.bigquery.Schema _schemaOverride;

    // The default values of configured BigQuery resource properties can be overridden by the values of CLI arguments.
    protected String
        GCP_DEFAULT_USER_PROJECT_ID, GCP_DEFAULT_USER_DATASET, GCP_DEFAULT_USER_TABLE,
        GCP_SA_PROJECT_ID, GCP_SA_DATASET, GCP_SA_TABLE;
    protected com.google.cloud.bigquery.Schema SCHEMA;

    protected RunEnvironment runEnvironment = RunEnvironment.builder().build();

    @BeforeEach
    public void before() {
        initialize();
        bqAdminClient = new BigQueryAdminClient(bqConfig, restTemplate);
        BigQueryUtil.logRunConfig();
    }

    private void initialize() {
        initRunEnvironmentDefaultValues();
        initRunEnvironmentOverriddenValues();
        initRunEnvironment();
        initRunEnvironmentActiveProperties();
    }

    private void initRunEnvironmentDefaultValues() {
        // Class-level initializers.
        gcpDefaultUserProjectIdDefault = bqConfig.getGcpDefaultUserProjectId();
        gcpDefaultUserDatasetDefault = bqConfig.getGcpDefaultUserDataset();
        gcpDefaultUserTableDefault = bqConfig.getGcpDefaultUserTable();

        gcpSaProjectIdDefault = bqConfig.getGcpSaProjectId();
        gcpSaDatasetDefault = bqConfig.getGcpSaDataset();
        gcpSaTableDefault = bqConfig.getGcpSaTable();

        schemaDefault = bqConfig.getSchemaDefault();

        // Set default run environment properties from Spring @Configuration classes.
        runEnvironment = RunEnvironment.builder()
            .gcpDefaultUserProjectIdDefault(bqConfig.getGcpDefaultUserProjectId())
            .gcpDefaultUserDatasetDefault(bqConfig.getGcpDefaultUserDataset())
            .gcpDefaultUserTableDefault(bqConfig.getGcpDefaultUserTable())
            .gcpSaProjectId(bqConfig.getGcpSaProjectId())
            .gcpSaDataset(bqConfig.getGcpSaDataset())
            .gcpSaTable(bqConfig.getGcpSaTable())
            .schema(BigQueryUtil.InlineSchemaTranslator.translate(bqConfig.getSchemaDefault(), bqConfig.getDataTypes()))
            .build();
    }

    private void initRunEnvironmentOverriddenValues() {
        gcpDefaultUserProjectIdCliOverride = System.getProperty(BigQueryIntegrationTestFixture.CLI_ARG_KEYS.GCP_DEFAULT_USER_PROJECT_ID.name());
        gcpDefaultUserDatasetCliOverride = System.getProperty(BigQueryIntegrationTestFixture.CLI_ARG_KEYS.GCP_DEFAULT_USER_DATASET.name());
        gcpDefaultUserTableCliOverride = System.getProperty(BigQueryIntegrationTestFixture.CLI_ARG_KEYS.GCP_DEFAULT_USER_TABLE.name());

        gcpSaProjectIdCliOverride = System.getProperty(BigQueryIntegrationTestFixture.CLI_ARG_KEYS.GCP_SA_PROJECT_ID.name());
        gcpSaDatasetCliOverride = System.getProperty(BigQueryIntegrationTestFixture.CLI_ARG_KEYS.GCP_SA_DATASET.name());
        gcpSaTableCliOverride = System.getProperty(BigQueryIntegrationTestFixture.CLI_ARG_KEYS.GCP_SA_TABLE.name());

        schemaOverrideString = System.getProperty(BigQueryIntegrationTestFixture.CLI_ARG_KEYS.SCHEMA.name());
        if (StringUtils.isNotEmpty(schemaOverrideString)) {
            _schemaOverride = BigQueryUtil.InlineSchemaTranslator.translate(schemaOverrideString, bqConfig.getDataTypes());
        }
    }

    private void initRunEnvironment() {
        // Run environment defaults.
        runEnvironment.setGcpDefaultUserProjectIdDefault(gcpDefaultUserProjectIdDefault);
        runEnvironment.setGcpDefaultUserDatasetDefault(gcpDefaultUserDatasetDefault);
        runEnvironment.setGcpDefaultUserTableDefault(gcpDefaultUserTableDefault);

        runEnvironment.setGcpSaProjectIdDefault(gcpSaProjectIdDefault);
        runEnvironment.setGcpSaDatasetDefault(gcpSaDatasetDefault);
        runEnvironment.setGcpSaTableDefault(gcpSaTableDefault);

        // Override default properties with values of CLI arguments.
        runEnvironment.setGcpDefaultUserProjectId(setEnvProperty(gcpDefaultUserProjectIdDefault, gcpDefaultUserProjectIdCliOverride));
        runEnvironment.setGcpDefaultUserDataset(setEnvProperty(gcpDefaultUserDatasetDefault, gcpDefaultUserDatasetCliOverride));
        runEnvironment.setGcpDefaultUserTable(setEnvProperty(gcpDefaultUserTableDefault, gcpDefaultUserTableCliOverride));

        runEnvironment.setGcpSaProjectId(setEnvProperty(gcpSaProjectIdDefault, gcpSaProjectIdCliOverride));
        runEnvironment.setGcpSaDataset(setEnvProperty(gcpSaDatasetDefault, gcpSaDatasetCliOverride));
        runEnvironment.setGcpSaTable(setEnvProperty(gcpSaTableDefault, gcpSaTableCliOverride));

        // Set table schema in the run environment.
        runEnvironment.setSchema(
            StringUtils.isNotEmpty(schemaOverrideString) ?
                _schemaOverride :
                BigQueryUtil.InlineSchemaTranslator.translate(schemaDefault, bqConfig.getDataTypes())
        );
    }

    private void initRunEnvironmentActiveProperties() {
        // Set integration test class level variables for active run environment.
        GCP_DEFAULT_USER_PROJECT_ID = runEnvironment.getGcpDefaultUserProjectId();
        GCP_DEFAULT_USER_DATASET = runEnvironment.getGcpDefaultUserDataset();
        GCP_DEFAULT_USER_TABLE = runEnvironment.getGcpDefaultUserTable();

        GCP_SA_PROJECT_ID = runEnvironment.getGcpSaProjectId();
        GCP_SA_DATASET = runEnvironment.getGcpSaDataset();
        GCP_SA_TABLE = runEnvironment.getGcpSaTable();

        SCHEMA = runEnvironment.getSchema();
    }

    private String setEnvProperty(String defaultValue, String overrideValue) {
        return null != overrideValue ? overrideValue : defaultValue;
    }

}
