package org.squidmin.java.spring.gradle.bigquery.fixture;

import org.squidmin.java.spring.gradle.bigquery.dao.RecordExample;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequest;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequestItem;
import org.squidmin.java.spring.gradle.bigquery.util.RunEnvironment;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class BigQueryIntegrationTestFixture {

    public enum CLI_ARG_KEYS {
        GCP_DEFAULT_USER_PROJECT_ID, GCP_DEFAULT_USER_DATASET, GCP_DEFAULT_USER_TABLE,
        GCP_SA_PROJECT_ID, GCP_SA_DATASET, GCP_SA_TABLE,
        SCHEMA
    }

    public static abstract class QUERIES {
        public static BiFunction<RunEnvironment, String, String> LOOK_UP_BY_ID = (runEnvironment, id) -> String.format(
            "SELECT * FROM %s.%s.%s WHERE id = '%s'",
            runEnvironment.getGcpDefaultUserProjectId(),
            runEnvironment.getGcpDefaultUserDataset(),
            runEnvironment.getGcpDefaultUserTable(),
            id
        );
    }

    public static final Supplier<List<RecordExample>> DEFAULT_ROWS = () -> IntStream.range(0, 5)
        .mapToObj(i -> {
            LocalDateTime now = LocalDateTime.now(TimeZone.getDefault().toZoneId());
            String creationTimestamp = LocalDateTime.of(
                2023, Month.JANUARY, 1,
                now.getHour(), now.getMinute(), now.getSecond()
            ).minusDays(3).format(DateTimeFormatter.ISO_DATE_TIME);
            String lastUpdateTimestamp = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(now.toEpochSecond(ZoneOffset.UTC)),
                TimeZone.getDefault().toZoneId()
            ).format(DateTimeFormatter.ISO_DATE_TIME);
            return RecordExample.builder()
                .id(UUID.randomUUID().toString())
                .creationTimestamp(creationTimestamp)
                .lastUpdateTimestamp(lastUpdateTimestamp)
                .columnA("asdf1")
                .columnB("asdf2")
                .build();
        }).collect(Collectors.toList());

    public static ExampleRequest validRequest() {
        LocalDateTime now = LocalDateTime.now(TimeZone.getDefault().toZoneId());
        String creationTimestamp = LocalDateTime.of(
            2023, Month.JANUARY, 1,
            now.getHour(), now.getMinute(), now.getSecond()
        ).minusDays(3).format(DateTimeFormatter.ISO_DATE_TIME);
        String lastUpdateTimestamp = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(now.toEpochSecond(ZoneOffset.UTC)),
            TimeZone.getDefault().toZoneId()
        ).format(DateTimeFormatter.ISO_DATE_TIME);
        return ExampleRequest.builder()
            .body(
                Collections.singletonList(
                    ExampleRequestItem.builder()
                        .id("asdf-1234")
                        .creationTimestamp(creationTimestamp)
                        .lastUpdateTimestamp(lastUpdateTimestamp)
                        .columnA("fdsa1")
                        .columnB(String.valueOf(false))
                        .build()
                )
            )
            .build();
    }

}
