package org.squidmin.java.spring.gradle.bigquery.service;

import autovalue.shaded.com.google.common.collect.ImmutableMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.paging.Page;
import com.google.cloud.bigquery.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.DataTypes;
import org.squidmin.java.spring.gradle.bigquery.dao.RecordExample;
import org.squidmin.java.spring.gradle.bigquery.dto.Query;
import org.squidmin.java.spring.gradle.bigquery.exception.CustomJobException;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryUtil;
import org.squidmin.java.spring.gradle.bigquery.config.ExampleSchema;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@EnableConfigurationProperties(value = {ExampleSchema.class, DataTypes.class})
public class BigQueryAdminClient {

    private final String projectId, datasetName, tableName;

    private final BigQuery bq;

    private final BigQueryConfig bqConfig;

    private final ObjectMapper mapper;

    @Autowired
    public BigQueryAdminClient(BigQueryConfig bqConfig) {
        this.bqConfig = bqConfig;
        this.bq = bqConfig.getBigQuery();
        this.projectId = bqConfig.getProjectId();
        this.datasetName = bqConfig.getDatasetName();
        this.tableName = bqConfig.getTableName();
        mapper = new ObjectMapper();
    }

    public void listDatasets(String projectId) {
        try {
            Page<Dataset> datasets = bq.listDatasets(projectId, BigQuery.DatasetListOption.pageSize(100));
            if (null == datasets) {
                Logger.log("Dataset does not contain any models.", Logger.LogType.CYAN);
                return;
            }
            AtomicInteger count = new AtomicInteger();
            datasets.iterateAll().forEach(
                dataset -> {
                    Logger.log(
                        String.format("Dataset ID: %s", dataset.getDatasetId()),
                        Logger.LogType.INFO
                    );
                    count.getAndIncrement();
                }
            );
            if (0 == count.get()) { Logger.log("Project does not contain any datasets.", Logger.LogType.ERROR); }
        } catch (BigQueryException e) {
            Logger.log("Project does not contain any datasets.", Logger.LogType.ERROR);
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
        }
    }

    public boolean datasetExists(String datasetName) {
        try {
            Dataset dataset = bq.getDataset(DatasetId.of(datasetName));
            if (dataset != null) {
                Logger.log("Dataset exists.", Logger.LogType.CYAN);
                return true;
            } else {
                Logger.log("Dataset not found.", Logger.LogType.CYAN);
            }
        } catch (BigQueryException e) {
            Logger.log("Something went wrong.", Logger.LogType.ERROR);
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
        }
        return false;
    }

    public boolean createDataset(String datasetName) {
        try {
            DatasetInfo datasetInfo = DatasetInfo.newBuilder(datasetName).build();
            Dataset newDataset = bq.create(datasetInfo);
            String newDatasetName = newDataset.getDatasetId().getDataset();
            Logger.log(String.format("Dataset %s created successfully.", newDatasetName), Logger.LogType.INFO);
        } catch (BigQueryException e) {
            Logger.log(
                String.format("%s: Dataset \"%s\" was not created.", e.getClass().getName(), datasetName),
                Logger.LogType.ERROR
            );
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
            return false;
        }
        return true;
    }

    public void deleteDataset(String projectId, String datasetName) {
        try {
            DatasetId datasetId = DatasetId.of(projectId, datasetName);
            boolean success = bq.delete(datasetId, BigQuery.DatasetDeleteOption.deleteContents());
            if (success) { Logger.log("Dataset deleted successfully", Logger.LogType.INFO); }
            else { Logger.log("Dataset was not found", Logger.LogType.INFO); }
        } catch (BigQueryException e) {
            Logger.log(String.format("Dataset '%s' was not deleted.", datasetName), Logger.LogType.ERROR);
        }
    }

    public void deleteDatasetAndContents(String projectId, String datasetName) {
        try {
            DatasetId datasetId = DatasetId.of(projectId, datasetName);
            // Use the force parameter to delete a dataset and its contents
            boolean success = bq.delete(datasetId, BigQuery.DatasetDeleteOption.deleteContents());
            if (success) { Logger.log("Dataset deleted with contents successfully", Logger.LogType.INFO); }
            else { Logger.log("Dataset was not found", Logger.LogType.INFO); }
        } catch (BigQueryException e) {
            Logger.log(String.format("Dataset '%s' was not deleted with contents.", datasetName), Logger.LogType.ERROR);
        }
    }

    public boolean createTable(String datasetName, String tableName) {
        return createTable(datasetName, tableName, BigQueryUtil.translate(bqConfig.getSchema()));
    }

    public boolean createTable(String datasetName, String tableName, Schema schema) {
        try {
            TableId tableId = TableId.of(datasetName, tableName);
            TableDefinition tableDefinition = StandardTableDefinition.of(schema);
            TableInfo tableInfo = TableInfo.newBuilder(tableId, tableDefinition).build();
            Logger.logCreateTable(tableInfo);
            bq.create(tableInfo);
            Logger.log("Table created successfully.", Logger.LogType.INFO);
        } catch (BigQueryException e) {
            Logger.log(
                String.format("%s: Table \"%s\" was not created.", e.getClass().getName(), tableName),
                Logger.LogType.ERROR
            );
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
            return false;
        }
        return true;
    }

    public void deleteTable(String projectId, String datasetName, String tableName) {
        try {
            boolean success = bq.delete(TableId.of(datasetName, tableName));
            if (success) { Logger.log("Table deleted successfully", Logger.LogType.INFO); }
            else { Logger.log("Table was not found", Logger.LogType.INFO); }
        } catch (BigQueryException e) {
            Logger.log(String.format("Table %s was not deleted.", tableName), Logger.LogType.ERROR);
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
        }
    }

    public void insert(List<RecordExample> records) {
        try {
            TableId tableId = TableId.of(datasetName, tableName);

            List<InsertAllRequest.RowToInsert> rows = new ArrayList<>();
            Map<String, Object> rowContent = new HashMap<>();
            for (RecordExample record : records) {
                rowContent.put("id", record.getId());
                rowContent.put("fieldA", record.getFieldA());
                rowContent.put("fieldB", record.getFieldB());
                rowContent.put("fieldC", record.getFieldC());
                rowContent.put("fieldD", record.getFieldD());
                InsertAllRequest.RowToInsert row = InsertAllRequest.RowToInsert.of(UUID.randomUUID().toString(), rowContent);
                rows.add(row);
            }

            InsertAllResponse response = bq.insertAll(
                InsertAllRequest.newBuilder(tableId)
                    .setIgnoreUnknownValues(true)
                    .setSkipInvalidRows(true)
                    .setRows(rows)
                    .build()
            );

            if (response.hasErrors()) {
                for (Map.Entry<Long, List<BigQueryError>> entry : response.getInsertErrors().entrySet()) {
                    Logger.log(String.format("Response error: %s", entry.getValue()), Logger.LogType.ERROR);
                }
            }
            Logger.log("Rows successfully inserted into table", Logger.LogType.INFO);
        } catch (BigQueryException e) {
            Logger.log("Insert operation not performed.", Logger.LogType.ERROR);
            Logger.log(String.format("%s", e), Logger.LogType.ERROR);
        }
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

    public ResponseEntity<String> restfulQuery(Query query) throws JsonProcessingException {
        String _query = mapper.writeValueAsString(query);
        Logger.log(String.format("QUERY == %s", _query), Logger.LogType.INFO);
        return null;
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

    public TableResult queryById(String id) {
        String template =
            "SELECT * " +
            "FROM `%s.%s.%s` " +
            "WHERE id = '%s'";
        String query = String.format(template, projectId, datasetName, tableName, id);
        return query(query);
    }

}
