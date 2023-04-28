package org.squidmin.java.spring.gradle.bigquery.config;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "org.squidmin.java.spring.gradle.bigquery"
})
@Getter
@Slf4j
public class BigQueryConfig {

    private final String projectId, datasetName, tableName;

    private final String baseUri;

    private final String queryEndpoint;

    private final ExampleSchema schema;

    private final BigQuery bigQuery;

    private final DataTypes dataTypes;

    @Autowired
    public BigQueryConfig(@Value("${bigquery.projectId}") String projectId,
                          @Value("${bigquery.datasetName}") String datasetName,
                          @Value("${bigquery.tableName}") String tableName,
                          @Value("${bigquery.rest-service.base-uri") String baseUri,
                          @Value("${bigquery.rest-service.endpoints.v2.jobs.query}") String queryEndpoint,
                          ExampleSchema schema,
                          DataTypes dataTypes) {
        this.projectId = projectId;
        this.datasetName = datasetName;
        this.tableName = tableName;

        this.baseUri = baseUri;
        this.queryEndpoint = queryEndpoint;

        this.schema = schema;
        this.dataTypes = dataTypes;

        bigQuery = BigQueryOptions.newBuilder().setLocation("us").build().getService();
    }

}
