package org.squidmin.java.spring.gradle.bigquery.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("integration")
@Slf4j
public class IntegrationTestConfig {

    @Value("${bigquery.projectId}")
    private String projectId;

    @Value("${bigquery.datasetName}")
    private String datasetName;

    @Value("${bigquery.tableName}")
    private String tableName;

    @Autowired
    private ExampleSchema schema;

    @Autowired
    private DataTypes dataTypes;

    private BigQueryConfig bqConfig;

    @Bean
    public BigQueryConfig bqConfig() {
        bqConfig = new BigQueryConfig(projectId, datasetName, tableName, schema, dataTypes);
        return bqConfig;
    }

    @Bean
    public ExampleSchema schema() { return schema; }

    @Bean
    public DataTypes dataTypes() {
        return dataTypes;
    }

}
