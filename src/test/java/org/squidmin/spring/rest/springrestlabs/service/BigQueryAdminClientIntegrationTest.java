package org.squidmin.spring.rest.springrestlabs.service;

import com.google.cloud.bigquery.TableResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.squidmin.spring.rest.springrestlabs.IntegrationTest;
import org.squidmin.spring.rest.springrestlabs.logger.Logger;
import org.squidmin.spring.rest.springrestlabs.util.BigQueryUtil;;

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
    public void createDataset() {
        bqAdminClient.createDataset(DATASET_NAME);
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
    public void query() {
        TableResult output = bqAdminClient.query("asdf-1234");
        Assertions.assertTrue(0 < output.getTotalRows());
    }

}
