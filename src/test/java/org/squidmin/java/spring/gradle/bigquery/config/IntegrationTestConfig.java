package org.squidmin.java.spring.gradle.bigquery.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SelectFieldsDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.WhereFieldsDefault;

@Configuration
@Profile("integration")
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

    @Autowired
    private SchemaDefault schemaDefault;

    @Autowired
    private DataTypes dataTypes;

    @Autowired
    private SelectFieldsDefault selectFieldsDefault;

    @Autowired
    private WhereFieldsDefault whereFieldsDefault;

    private BigQueryConfig bqConfig;

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
    public SchemaDefault schema() {
        return schemaDefault;
    }

    @Bean
    public DataTypes dataTypes() {
        return dataTypes;
    }

    @Bean
    public SelectFieldsDefault selectFields() {
        return selectFieldsDefault;
    }

    @Bean
    public WhereFieldsDefault whereFields() {
        return whereFieldsDefault;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public BigQueryConfig bqConfig() {
        bqConfig = new BigQueryConfig(
            gcpDefaultUserProjectId,
            gcpDefaultUserDataset,
            gcpDefaultUserTable,
            gcpSaProjectId,
            gcpSaDataset,
            gcpSaTable,
            queryUri,
            schemaDefault,
            dataTypes,
            selectFieldsDefault,
            whereFieldsDefault
        );
        return bqConfig;
    }

}
