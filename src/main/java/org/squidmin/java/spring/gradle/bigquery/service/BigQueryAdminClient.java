package org.squidmin.java.spring.gradle.bigquery.service;

import autovalue.shaded.com.google.common.collect.ImmutableMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.paging.Page;
import com.google.cloud.bigquery.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.DataTypes;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SelectFieldsDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.WhereFieldsDefault;
import org.squidmin.java.spring.gradle.bigquery.dao.RecordExample;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponse;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponseItem;
import org.squidmin.java.spring.gradle.bigquery.dto.Query;
import org.squidmin.java.spring.gradle.bigquery.exception.CustomJobException;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryUtil;
import org.squidmin.java.spring.gradle.bigquery.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@EnableConfigurationProperties(value = {
    SchemaDefault.class,
    SelectFieldsDefault.class,
    WhereFieldsDefault.class,
    DataTypes.class,
})
public class BigQueryAdminClient {

    private final String gcpDefaultUserProjectId, gcpDefaultUserDataset, gcpDefaultUserTable;
    private final String gcpSaProjectId, gcpSaDataset, gcpSaTable;

    private final BigQuery bq;

    private final BigQueryConfig bqConfig;

    private final RestTemplate restTemplate;

    private final ObjectMapper mapper;

    @Autowired
    public BigQueryAdminClient(BigQueryConfig bqConfig, RestTemplate restTemplate) {
        this.bqConfig = bqConfig;
        this.bq = bqConfig.getBigQuery();
        this.gcpDefaultUserProjectId = bqConfig.getGcpDefaultUserProjectId();
        this.gcpDefaultUserDataset = bqConfig.getGcpDefaultUserDataset();
        this.gcpDefaultUserTable = bqConfig.getGcpDefaultUserTable();
        this.gcpSaProjectId = bqConfig.getGcpSaProjectId();
        this.gcpSaDataset = bqConfig.getGcpSaDataset();
        this.gcpSaTable = bqConfig.getGcpSaTable();
        this.restTemplate = restTemplate;
        mapper = new ObjectMapper();
    }

    public void listDatasets() {
        try {
            Page<Dataset> datasets = bq.listDatasets(gcpDefaultUserProjectId, BigQuery.DatasetListOption.pageSize(100));
            if (null == datasets) {
                Logger.log(String.format("Dataset \"%s\" does not contain any models.", gcpDefaultUserDataset), Logger.LogType.ERROR);
                return;
            }
            BigQueryUtil.logDatasets(gcpDefaultUserProjectId, datasets);
        } catch (BigQueryException e) {
            Logger.log(String.format("Project \"%s\" does not contain any datasets.", gcpDefaultUserProjectId), Logger.LogType.ERROR);
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
        }
    }

    public boolean createDataset(String dataset) {
        try {
            DatasetInfo datasetInfo = DatasetInfo.newBuilder(dataset).build();
            Dataset newDataset = bq.create(datasetInfo);
            String newDatasetName = newDataset.getDatasetId().getDataset();
            Logger.log(String.format("Dataset \"%s\" created successfully.", newDatasetName), Logger.LogType.INFO);
        } catch (BigQueryException e) {
            Logger.log(
                String.format("%s: Dataset \"%s\" was not created.", e.getClass().getName(), dataset),
                Logger.LogType.ERROR
            );
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
            return false;
        }
        return true;
    }

    public void deleteDataset(String projectId, String dataset) {
        try {
            DatasetId datasetId = DatasetId.of(projectId, dataset);
            boolean success = bq.delete(datasetId);
            if (success) {
                Logger.log(String.format("Dataset \"%s\" deleted successfully.", dataset), Logger.LogType.INFO);
            } else {
                Logger.log(String.format("Dataset \"%s\" was not found in project \"%s\".", dataset, projectId), Logger.LogType.INFO);
            }
        } catch (BigQueryException e) {
            Logger.log(String.format("Dataset \"%s\" was not deleted.", dataset), Logger.LogType.ERROR);
        }
    }

    public void deleteDatasetAndContents(String projectId, String dataset) {
        try {
            DatasetId datasetId = DatasetId.of(projectId, dataset);
            boolean success = bq.delete(datasetId, BigQuery.DatasetDeleteOption.deleteContents());
            if (success) {
                Logger.log(String.format("Dataset \"%s\" and its contents deleted successfully.", dataset), Logger.LogType.INFO);
            } else {
                Logger.log(String.format("Dataset \"%s\" was not found in project \"%s\".", dataset, projectId), Logger.LogType.INFO);
            }
        } catch (BigQueryException e) {
            Logger.log(String.format("Dataset \"%s\" was not deleted with contents.", dataset), Logger.LogType.ERROR);
        }
    }

    public boolean createTable(String dataset, String table) {
        Schema schema = BigQueryUtil.InlineSchemaTranslator.translate(bqConfig.getSchemaDefault(), bqConfig.getDataTypes());
        return createTable(dataset, table, schema);
    }

    public boolean createTable(String dataset, String table, Schema schema) {
        try {
            TableId tableId = TableId.of(dataset, table);
            TableDefinition tableDefinition = StandardTableDefinition.of(schema);
            TableInfo tableInfo = TableInfo.newBuilder(tableId, tableDefinition).build();
            BigQueryUtil.logCreateTable(tableInfo);
            bq.create(tableInfo);
            Logger.log(String.format("Table \"%s\" created successfully.", table), Logger.LogType.INFO);
        } catch (BigQueryException e) {
            Logger.log(
                String.format("%s: Table \"%s\" was not created.", e.getClass().getName(), table),
                Logger.LogType.ERROR
            );
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
            return false;
        }
        return true;
    }

    public void deleteTable(String projectId, String dataset, String table) {
        try {
            boolean success = bq.delete(TableId.of(projectId, dataset, table));
            if (success) {
                Logger.log("Table deleted successfully", Logger.LogType.INFO);
            } else {
                Logger.log("Table was not found", Logger.LogType.INFO);
            }
        } catch (BigQueryException e) {
            Logger.log(String.format("Table %s was not deleted.", table), Logger.LogType.ERROR);
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
        }
    }

    public List<InsertAllRequest.RowToInsert> insert(String projectId, String dataset, String table, List<RecordExample> records) {
        try {
            List<InsertAllRequest.RowToInsert> rowsToInsert = new ArrayList<>();

            TableId tableId = TableId.of(projectId, dataset, table);

            Map<String, Object> rowContent = new HashMap<>();

            records.forEach(record -> {
                bqConfig.getSchemaDefault().getFields().forEach(field -> {
                    String fieldName = field.getName();
                    rowContent.put(fieldName, record.getField(fieldName));
                });
                rowsToInsert.add(InsertAllRequest.RowToInsert.of(UUID.randomUUID().toString(), rowContent));
            });

            InsertAllResponse response = bq.insertAll(
                InsertAllRequest.newBuilder(tableId)
                    .setIgnoreUnknownValues(true)
                    .setSkipInvalidRows(true)
                    .setRows(rowsToInsert)
                    .build()
            );

            if (response.hasErrors()) {
                for (Map.Entry<Long, List<BigQueryError>> entry : response.getInsertErrors().entrySet()) {
                    Logger.log(String.format("Response error: %s", entry.getValue()), Logger.LogType.ERROR);
                }
                return Collections.emptyList();
            } else {
                Logger.log("Rows successfully inserted into table", Logger.LogType.INFO);
                return rowsToInsert;
            }
        } catch (BigQueryException e) {
            Logger.log("Insert operation not performed.", Logger.LogType.ERROR);
            Logger.log(String.format("%s", e), Logger.LogType.ERROR);
        }
        return Collections.emptyList();
    }

    public TableResult query(String query) {
        try {
            // Set optional job resource properties.
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                .setLabels(ImmutableMap.of("example-label", "example-value"))
                .build();

            // The location and job name are optional, if both are not specified then client will auto-create.
            String jobName = "jobId_" + UUID.randomUUID();
            JobId jobId = JobId.newBuilder().setLocation("us").setJob(jobName).build();

            bq.create(JobInfo.of(jobId, queryConfig));  // Create a job with job ID.

            Job job = bq.getJob(jobId);  // Get the job that was just created.
            String _job = job.getJobId().getJob();
            TableResult tableResult;
            if (null != _job && _job.equals(jobId.getJob())) {
                Logger.log(
                    String.format("%s: Job \"%s\" was created successfully.", this.getClass().getName(), _job),
                    Logger.LogType.INFO
                );
                job.waitFor();
                if (null != job.getStatus().getError()) {
                    throw new CustomJobException(job.getStatus().getError().getMessage());
                }
            } else {
                Logger.log("Job was not created.", Logger.LogType.ERROR);
            }
            tableResult = job.getQueryResults();
            Logger.log("Query performed successfully.", Logger.LogType.INFO);
            return tableResult;
        } catch (BigQueryException | InterruptedException | CustomJobException e) {
            Logger.log(
                String.format("%s: Job \"%s\" was not created.", e.getClass().getName(), e.getMessage()),
                Logger.LogType.ERROR
            );
        }
        return new EmptyTableResult(Schema.of());
    }

    public ResponseEntity<ExampleResponse> restfulQuery(Query query) throws IOException {
        String _query = mapper.writeValueAsString(query);
        Logger.log(String.format("QUERY == %s", _query), Logger.LogType.INFO);
        String uri = bqConfig.getQueryUri();
        HttpHeaders httpHeaders = getHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(query), httpHeaders);
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(uri, request, String.class);
            String responseBody = responseEntity.getBody();
            if (!StringUtils.isEmpty(responseBody)) {
                return new ResponseEntity<>(
                    ExampleResponse.builder()
                        .body(BigQueryUtil.toList(responseBody.getBytes(StandardCharsets.UTF_8), bqConfig.getSelectFieldsDefault(), true))
                        .build(),
                    HttpStatus.OK
                );
            } else {
                Logger.log("Response body is empty.", Logger.LogType.ERROR);
            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getMessage();
            Logger.log(errorMessage, Logger.LogType.ERROR);
            return new ResponseEntity<>(
                ExampleResponse.builder()
                    .body(Collections.singletonList(ExampleResponseItem.builder().build()))
                    .errors(Collections.singletonList(errorMessage))
                    .build(),
                e.getStatusCode()
            );
        }
        return new ResponseEntity<>(ExampleResponse.builder().build(), HttpStatus.OK);
    }

    public TableResult queryBatch(String query) {
        try {
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                // Run at batch priority, which won't count toward concurrent rate limit.
                .setPriority(QueryJobConfiguration.Priority.BATCH)
                .build();
            Logger.log("Query batch performed successfully.", Logger.LogType.INFO);
            return bq.query(queryConfig);
        } catch (BigQueryException | InterruptedException e) {
            Logger.log("Query batch not performed:", Logger.LogType.ERROR);
            Logger.log(String.format("%s", e.getMessage()), Logger.LogType.ERROR);
        }
        return null;
    }


    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer ".concat(bqConfig.getGcpAdcAccessToken()));
        httpHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return httpHeaders;
    }

}
