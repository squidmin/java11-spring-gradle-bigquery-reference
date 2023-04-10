package org.squidmin.spring.rest.springrestlabs.service;

import com.google.cloud.bigquery.TableResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.squidmin.spring.rest.springrestlabs.IntegrationTest;
import org.squidmin.spring.rest.springrestlabs.fixture.BigQueryFixture;
import org.squidmin.spring.rest.springrestlabs.logger.Logger;
import org.squidmin.spring.rest.springrestlabs.util.BigQueryUtil;

@Slf4j
public class BigQueryAdminClientIntegrationTest extends IntegrationTest {

    @Test
    public void echoDefaultBigQueryResourceMetadata() {
        Logger.echoBqResourceMetadata(bqResourceMetadata, Logger.ProfileOption.DEFAULT);
    }

    @Test
    public void listDatasets() {
        bqAdminClient.listDatasets(PROJECT_ID);
    }

    @Test
    public void datasetExists() {
        bqAdminClient.datasetExists(bqResourceMetadata.getDatasetName());
    }

    @Test
    public void createDataset() {
        bqAdminClient.createDataset(DATASET_NAME);
    }

    @Test
    public void deleteDataset() {
        bqAdminClient.deleteDataset(
            bqResourceMetadata.getProjectId(),
            bqResourceMetadata.getDatasetName()
        );
    }

    @Test
    public void deleteDatasetAndContents() {
        bqAdminClient.deleteDatasetAndContents(
            bqResourceMetadata.getProjectId(),
            bqResourceMetadata.getDatasetName()
        );
    }

    @Test
    public void createTableWithDefaultSchema() {
        Logger.echoBqResourceMetadata(bqResourceMetadata, Logger.ProfileOption.ACTIVE);
        Assertions.assertTrue(
            bqAdminClient.createTable(DATASET_NAME, TABLE_NAME)
        );
    }

    @Test
    public void createTableWithCustomSchema() {
        Logger.echoBqResourceMetadata(bqResourceMetadata, Logger.ProfileOption.ACTIVE);
        Assertions.assertTrue(
            bqAdminClient.createTable(
                DATASET_NAME,
                TABLE_NAME,
                BigQueryUtil.translate(schemaOverride)
            )
        );
    }

    @Test
    public void deleteTable() {
        bqAdminClient.deleteTable(
            bqResourceMetadata.getProjectId(),
            bqResourceMetadata.getDatasetName(),
            bqResourceMetadata.getTableName()
        );
    }

    @Test
    public void insert() {
        bqAdminClient.insert(BigQueryFixture.DEFAULT_ROWS.get());
    }

    @Test
    public void query() {
        TableResult output = bqAdminClient.query(
            BigQueryFixture.QUERIES.LOOK_UP_ID.apply(bqResourceMetadata, "asdf-1234")
        );
        Logger.log("Found rows:", Logger.LogType.INFO);
        output.iterateAll().forEach(row -> Logger.log(row.toString(), Logger.LogType.INFO));
    }

    @Test
    public void queryBatch() {
        TableResult output = bqAdminClient.queryBatch(
            BigQueryFixture.QUERIES.LOOK_UP_ID.apply(bqResourceMetadata, "asdf-1234")
        );
        Logger.log("Found rows:", Logger.LogType.INFO);
        output.iterateAll().forEach(row -> Logger.log(row.toString(), Logger.LogType.INFO));
    }

    @Test
    public void queryById() {
        TableResult output = bqAdminClient.queryById("asdf-1234");
        Logger.log("Found rows:", Logger.LogType.INFO);
        output.iterateAll().forEach(row -> Logger.log(row.toString(), Logger.LogType.INFO));
//        Assertions.assertTrue(0 < output.getTotalRows());
    }

}
