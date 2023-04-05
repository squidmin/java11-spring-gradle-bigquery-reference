package org.squidmin.spring.rest.springrestlabs.util;

import com.google.cloud.bigquery.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BigQueryResourceMetadata {

    private String projectIdDefault;
    private String datasetNameDefault;
    private String tableNameDefault;
    private String projectIdOverride;
    private String datasetNameOverride;
    private String tableNameOverride;

    private String projectId;
    private String datasetName;
    private String tableName;

    private Schema schema;

}
