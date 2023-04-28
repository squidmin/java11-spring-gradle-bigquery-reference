package org.squidmin.java.spring.gradle.bigquery.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.cloud.bigquery.TableResult;
import org.springframework.stereotype.Repository;
import org.squidmin.java.spring.gradle.bigquery.dao.RecordExample;
import org.squidmin.java.spring.gradle.bigquery.dto.Query;
import org.squidmin.java.spring.gradle.bigquery.dto.ResponseExample;
import org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClient;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryUtil;

import java.util.List;

@Repository
public class ExampleRepositoryImpl implements ExampleRepository {

    private final BigQueryAdminClient bqAdminClient;

    public ExampleRepositoryImpl(BigQueryAdminClient bqAdminClient) {
        this.bqAdminClient = bqAdminClient;
    }

    @Override
    public List<ResponseExample> findById(String id) {
        TableResult tableResult = bqAdminClient.queryById(id);
        List<ResponseExample> queryResult = BigQueryUtil.toList(tableResult);
        return queryResult;
    }

    @Override
    public ResponseExample query(Query query) throws JsonProcessingException {
        ResponseExample response = bqAdminClient.restfulQuery(query);
        return response;
    }

    @Override
    public int insert(List<RecordExample> records) {
        int numRowsInserted = bqAdminClient.insert(records).size();
        return 0 < numRowsInserted ? numRowsInserted : -1;
    }

}
