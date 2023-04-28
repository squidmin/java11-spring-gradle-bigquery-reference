package org.squidmin.java.spring.gradle.bigquery.util;

import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.Schema;
import com.google.cloud.bigquery.StandardSQLTypeName;
import com.google.cloud.bigquery.TableResult;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.squidmin.java.spring.gradle.bigquery.config.ExampleSchema;
import org.squidmin.java.spring.gradle.bigquery.dto.ResponseExample;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BigQueryUtil {

    public static Schema translate(ExampleSchema exampleSchema) {
        Logger.log(String.format("Generating Schema object using: \"%s\"...", exampleSchema.getFields()), Logger.LogType.CYAN);
        List<Field> fields = new ArrayList<>();
        exampleSchema.getFields().forEach(
            f -> {
                log.info("name={}, type={}", f.getName(), f.getType());
                fields.add(Field.of(f.getName(), translateType(f.getType())));
            }
        );
        Logger.log("Finished generating schema.", Logger.LogType.CYAN);
        return Schema.of(fields);
    }

    public static Schema translate(String schema) {
        if (!StringUtils.isEmpty(schema)) {
            Logger.log(String.format("Generating Schema object using CLI arg: \"%s\"...", schema), Logger.LogType.CYAN);
            List<Field> fields = new ArrayList<>();
            List<String> _fields = Arrays.stream(schema.split(";")).collect(Collectors.toList());
            if (0 < _fields.size()) {
                _fields.forEach(
                    f -> {
                        String[] split = f.split(",");
                        String name = split[0], type = split[1];
                        Logger.log(String.format("name=%s, type=%s", name, type), Logger.LogType.DEBUG);
                        fields.add(Field.of(name, translateType(type)));
                    }
                );
            }
            Logger.log("Finished generating schema.", Logger.LogType.CYAN);
            return Schema.of(fields);
        }
        return Schema.of();
    }

    private static StandardSQLTypeName translateType(String type) {
        if (type.equalsIgnoreCase("string")) {
            return StandardSQLTypeName.STRING;
        } else if (type.equalsIgnoreCase("datetime")) {
            return StandardSQLTypeName.DATETIME;
        } else if (type.equalsIgnoreCase("bool")) {
            return StandardSQLTypeName.BOOL;
        } else {
            Logger.log(
                "Error: translateType(): Data type not supported. Defaulting to 'StandardSQLTypeName.STRING'.",
                Logger.LogType.ERROR
            );
            return StandardSQLTypeName.STRING;
        }
    }

    public static List<ResponseExample> toList(TableResult tableResult) {
        List<ResponseExample> response = new ArrayList<>();
        if (null != tableResult && 0 < tableResult.getTotalRows()) {
            tableResult.iterateAll().forEach(
                row -> response.add(
                    ResponseExample.builder()
                        .id(row.get(0).getStringValue())
                        .fieldA(row.get(1).getStringValue())
                        .fieldB(row.get(2).getStringValue())
                        .build()
                )
            );
        }
        return response;
    }

    public static List<ResponseExample> toList(byte[] tableResult) {
        // TODO: Implement toList() for response from GCP BigQuery REST API.
        return null;
    }

}
