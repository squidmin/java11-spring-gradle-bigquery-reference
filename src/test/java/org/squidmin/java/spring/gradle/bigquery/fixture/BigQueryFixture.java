package org.squidmin.java.spring.gradle.bigquery.fixture;

import org.squidmin.java.spring.gradle.bigquery.dao.RecordExample;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryResourceMetadata;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class BigQueryFixture {

    public enum CLI_ARG_KEYS {projectId, datasetName, tableName, schema}

    public static abstract class QUERIES {
        public static BiFunction<BigQueryResourceMetadata, String, String> LOOK_UP_ID = (bqResourceMetadata, id) -> String.format(
            "SELECT * FROM %s.%s.%s WHERE id = '%s'",
            bqResourceMetadata.getProjectId(),
            bqResourceMetadata.getDatasetName(),
            bqResourceMetadata.getTableName(),
            id
        );
    }

    public static final Supplier<List<RecordExample>> DEFAULT_ROWS = () -> IntStream.range(0, 5)
        .mapToObj(i -> RecordExample.builder()
            .id(UUID.randomUUID().toString())
            .fieldA("valueA")
            .fieldB("valueB")
            .fieldC("valueC")
            .fieldD("valueD")
            .build()
        ).collect(Collectors.toList());

}
