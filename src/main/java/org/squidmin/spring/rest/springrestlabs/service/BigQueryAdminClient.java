package org.squidmin.spring.rest.springrestlabs.service;

import autovalue.shaded.com.google.common.collect.ImmutableMap;
import com.google.api.gax.paging.Page;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryError;
import com.google.cloud.bigquery.BigQueryException;
import com.google.cloud.bigquery.Dataset;
import com.google.cloud.bigquery.DatasetInfo;
import com.google.cloud.bigquery.EmptyTableResult;
import com.google.cloud.bigquery.InsertAllRequest;
import com.google.cloud.bigquery.InsertAllResponse;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.Schema;
import com.google.cloud.bigquery.StandardTableDefinition;
import com.google.cloud.bigquery.TableDefinition;
import com.google.cloud.bigquery.TableId;
import com.google.cloud.bigquery.TableInfo;
import com.google.cloud.bigquery.TableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.squidmin.spring.rest.springrestlabs.config.BigQueryConfig;
import org.squidmin.spring.rest.springrestlabs.config.DataTypes;
import org.squidmin.spring.rest.springrestlabs.config.Field;
import org.squidmin.spring.rest.springrestlabs.dao.RecordExample;
import org.squidmin.spring.rest.springrestlabs.exception.CustomJobException;
import org.squidmin.spring.rest.springrestlabs.logger.Logger;
import org.squidmin.spring.rest.springrestlabs.util.BigQueryUtil;
import org.squidmin.spring.rest.springrestlabs.config.ExampleSchema;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@EnableConfigurationProperties(value = {ExampleSchema.class, DataTypes.class})
public class BigQueryAdminClient {

    private String projectId, datasetName, tableName;

    private final BigQuery bq;

    private final BigQueryConfig bqConfig;

    @Autowired
    public BigQueryAdminClient(BigQueryConfig bqConfig) {
        this.bqConfig = bqConfig;
        this.bq = bqConfig.getBigQuery();
        this.projectId = bqConfig.getProjectId();
        this.datasetName = bqConfig.getDatasetName();
        this.tableName = bqConfig.getTableName();
    }

    public void listDatasets() {
        listDatasets(projectId);
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

    public boolean createDataset() {
        return createDataset(datasetName);
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
            // Specify a job configuration to set optional job resource properties.
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

    public TableResult queryBatch(String query) {
        try {
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                    // Run at batch priority, which won't count toward concurrent rate limit.
                    .setPriority(QueryJobConfiguration.Priority.BATCH)
                    .build();
            Logger.log("Query batch performed successfully.", Logger.LogType.INFO);
            TableResult results = bq.query(queryConfig);
            return results;
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
