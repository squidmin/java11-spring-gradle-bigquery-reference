package org.squidmin.spring.rest.springrestlabs.logger;

import com.google.cloud.bigquery.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.squidmin.spring.rest.springrestlabs.util.BigQueryResourceMetadata;

@Slf4j
public class Logger {

    public static short HORIZONTAL_LINE_WIDTH = 50;

    public enum LogType { INFO, DEBUG, ERROR, CYAN }
    public static void log(String str, LogType logType) {
        if (logType == LogType.INFO) {
            str = LogFont.boldGreen(str);
            log.info(str);
        } else if (logType == LogType.DEBUG) {
            str = LogFont.bold(str);
            log.debug(str);
        } else if (logType == LogType.ERROR) {
            log.error(str);
        } else if (logType == LogType.CYAN) {
            str = LogFont.boldCyan(str);
            log.info(str);
        } else {
            log.error("Error: Logger: Invalid LogType.");
        }
    }

    public static void logTableInfo(TableInfo tableInfo) {
        log.info("Friendly name: " + tableInfo.getFriendlyName());
        log.info("Description: " + tableInfo.getDescription());
        log.info("Creation time: " + tableInfo.getCreationTime());
        log.info("Expiration time: " + tableInfo.getExpirationTime());
    }

    public static void logCreateTable(TableInfo tableInfo) {
        echoHorizontalLine(LogType.INFO);
        log(
            String.format("Creating table \"%s\". Find the table information below:", tableInfo.getTableId()),
            LogType.INFO
        );
        logTableInfo(tableInfo);
        echoHorizontalLine(LogType.INFO);
    }

    public static void echoBqResourceMetadata(String projectId, String datasetName, String tableName) {
        log(String.format("Project ID: %s", projectId), LogType.INFO);
        log(String.format("Dataset name: %s", datasetName), LogType.INFO);
        log(String.format("Table name: %s", tableName), LogType.INFO);
    }

    public enum ProfileOption { DEFAULT, OVERRIDDEN, ACTIVE }
    public static void echoBqResourceMetadata(BigQueryResourceMetadata bqResourceMetadata, ProfileOption profileOption) {
        echoHorizontalLine(LogType.INFO);
        if (profileOption == ProfileOption.DEFAULT) {
            log("--- BigQuery default properties ---", LogType.CYAN);
            echoBqResourceMetadata(
                bqResourceMetadata.getProjectIdDefault(),
                bqResourceMetadata.getDatasetNameDefault(),
                bqResourceMetadata.getTableNameDefault()
            );
        } else if (profileOption == ProfileOption.OVERRIDDEN) {
            log("--- BigQuery overridden properties ---", LogType.CYAN);
            echoBqResourceMetadata(
                bqResourceMetadata.getProjectIdOverride(),
                bqResourceMetadata.getDatasetNameOverride(),
                bqResourceMetadata.getTableNameOverride()
            );
        } else if (profileOption == ProfileOption.ACTIVE) {
            log("BigQuery resource properties currently configured:", LogType.CYAN);
            echoBqResourceMetadata(
                bqResourceMetadata.getProjectId(),
                bqResourceMetadata.getDatasetName(),
                bqResourceMetadata.getTableName()
            );
        } else {
            log.error("Error: IntegrationTest.echoBigQueryResourceMetadata(): Invalid option specified.");
        }
        echoHorizontalLine(LogType.INFO);
    }

    public static void echoHorizontalLine(LogType logType) {
        log("-".repeat(HORIZONTAL_LINE_WIDTH), logType);
    }

}
