package org.squidmin.spring.rest.springrestlabs.fixture;

public class BigQueryResourceFixture extends BigQueryFixture {

    private String projectId, datasetName, tableName;

    public BigQueryResourceFixture(String projectId, String datasetName, String tableName) {
        this.projectId = projectId;
        this.datasetName = datasetName;
        this.tableName = tableName;
    }

}
