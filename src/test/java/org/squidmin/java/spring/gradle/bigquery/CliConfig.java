package org.squidmin.java.spring.gradle.bigquery;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.Schema;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryOptionsConfig;
import org.squidmin.java.spring.gradle.bigquery.config.IntegrationTestConfig;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClient;
import org.squidmin.java.spring.gradle.bigquery.util.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest(classes = {BigQueryAdminClient.class, IntegrationTestConfig.class})
@ActiveProfiles("integration")
@Slf4j
public abstract class CliConfig {

    private static final String RESOURCES_BASE_PATH = "src/test/resources/";

    @Autowired
    @Qualifier("bigQueryConfig_integrationTest")
    protected BigQueryConfig bigQueryConfig;
    @Autowired
    @Qualifier("bigQueryConfigMock_integrationTest")
    protected BigQueryConfig bigQueryConfigMock;

    @Autowired
    @Qualifier("bigQueryOptionsConfig_integrationTest")
    protected BigQueryOptionsConfig bigQueryOptionsConfig;
    @Autowired
    @Qualifier("bigQueryOptionsConfigMock_integrationTest")
    protected BigQueryOptionsConfig bigQueryOptionsConfigMock;

    @Autowired
    protected BigQueryAdminClient bigQueryAdminClient;

    @Autowired
    @Qualifier("bigQuery_integrationTest")
    protected BigQuery bigQuery;
    @Autowired
    @Qualifier("bigQueryMock_integrationTest")
    protected BigQuery bigQueryMock;

    @Autowired
    @Qualifier("restTemplateMock_integrationTest")
    protected RestTemplate restTemplateMock;

    @Autowired
    @Qualifier("bigQueryTimeUtilMock_integrationTest")
    protected BigQueryTimeUtil bigQueryTimeUtilMock;

    @Autowired
    protected TemplateCompiler templateCompiler;

    protected String gcpDefaultUserProjectIdDefault;
    protected String gcpDefaultUserDatasetDefault;
    protected String gcpDefaultUserTableDefault;
    protected String gcpDefaultUserProjectIdCliOverride;
    protected String gcpDefaultUserDatasetCliOverride;
    protected String gcpDefaultUserTableCliOverride;
    protected String gcpSaProjectIdDefault;
    protected String gcpSaDatasetDefault;
    protected String gcpSaTableDefault;
    protected String gcpSaProjectIdCliOverride;
    protected String gcpSaDatasetCliOverride;
    protected String gcpSaTableCliOverride;
    protected String schemaOverrideString;

    protected SchemaDefault schemaDefault;
    protected Schema _schemaOverride;

    // The default values of configured BigQuery resource properties can be overridden by the values of CLI arguments.
    protected String GCP_DEFAULT_USER_PROJECT_ID;
    protected String GCP_DEFAULT_USER_DATASET;
    protected String GCP_DEFAULT_USER_TABLE;
    protected String GCP_SA_PROJECT_ID;
    protected String GCP_SA_DATASET;
    protected String GCP_SA_TABLE;
    protected Schema SCHEMA;

    protected RunEnvironment runEnvironment = RunEnvironment.builder().build();

    @BeforeEach
    public void beforeEach() {
        initialize();
        LoggerUtil.logRunConfig();
    }

    private void initialize() {
        initRunEnvironmentDefaultValues();
        initRunEnvironmentOverriddenValues();
        initRunEnvironment();
        initRunEnvironmentActiveProperties();
    }

    private void initRunEnvironmentDefaultValues() {
        // Class-level initializers.
        gcpDefaultUserProjectIdDefault = bigQueryConfig.getGcpDefaultUserProjectId();
        gcpDefaultUserDatasetDefault = bigQueryConfig.getGcpDefaultUserDataset();
        gcpDefaultUserTableDefault = bigQueryConfig.getGcpDefaultUserTable();

        gcpSaProjectIdDefault = bigQueryConfig.getGcpSaProjectId();
        gcpSaDatasetDefault = bigQueryConfig.getGcpSaDataset();
        gcpSaTableDefault = bigQueryConfig.getGcpSaTable();

        schemaDefault = bigQueryConfig.getSchemaDefault();

        // Set default run environment properties from Spring @Configuration classes.
        runEnvironment = RunEnvironment.builder()
            .gcpDefaultUserProjectIdDefault(bigQueryConfig.getGcpDefaultUserProjectId())
            .gcpDefaultUserDatasetDefault(bigQueryConfig.getGcpDefaultUserDataset())
            .gcpDefaultUserTableDefault(bigQueryConfig.getGcpDefaultUserTable())
            .gcpSaProjectId(bigQueryConfig.getGcpSaProjectId())
            .gcpSaDataset(bigQueryConfig.getGcpSaDataset())
            .gcpSaTable(bigQueryConfig.getGcpSaTable())
            .schema(BigQueryUtil.InlineSchemaTranslator.translate(bigQueryConfig.getSchemaDefault(), bigQueryConfig.getDataTypes()))
            .build();
    }

    private void initRunEnvironmentOverriddenValues() {
        gcpDefaultUserProjectIdCliOverride = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.GCP_DEFAULT_USER_PROJECT_ID.name());
        gcpDefaultUserDatasetCliOverride = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.GCP_DEFAULT_USER_DATASET.name());
        gcpDefaultUserTableCliOverride = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.GCP_DEFAULT_USER_TABLE.name());

        gcpSaProjectIdCliOverride = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.GCP_SA_PROJECT_ID.name());
        gcpSaDatasetCliOverride = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.GCP_SA_DATASET.name());
        gcpSaTableCliOverride = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.GCP_SA_TABLE.name());

        schemaOverrideString = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.SCHEMA.name());
        if (StringUtils.isNotEmpty(schemaOverrideString)) {
            _schemaOverride = BigQueryUtil.InlineSchemaTranslator.translate(schemaOverrideString, bigQueryConfig.getDataTypes());
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
                BigQueryUtil.InlineSchemaTranslator.translate(schemaDefault, bigQueryConfig.getDataTypes())
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

    public static String readJson(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(RESOURCES_BASE_PATH.concat(path))));
    }

    public static String readQueryString(String path) throws IOException {
        return BigQueryUtil.trimWhitespace(
            new String(
                Files.readAllBytes(
                    Paths.get(
                        RESOURCES_BASE_PATH.concat("queries/").concat(path)
                    )
                )
            )
        );
    }

}

