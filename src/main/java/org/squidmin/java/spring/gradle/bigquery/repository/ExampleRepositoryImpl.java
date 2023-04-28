package org.squidmin.java.spring.gradle.bigquery.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.cloud.bigquery.InsertAllRequest;
import com.google.cloud.bigquery.TableResult;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.squidmin.java.spring.gradle.bigquery.dao.RecordExample;
import org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClient;
import org.squidmin.java.spring.gradle.bigquery.dto.Query;
import org.squidmin.java.spring.gradle.bigquery.dto.RequestExample;
import org.squidmin.java.spring.gradle.bigquery.dto.ResponseExample;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryUtil;

import java.util.Collections;
import java.util.List;

@Repository
public class ExampleRepositoryImpl implements ExampleRepository {

    private final BigQueryAdminClient bqAdminClient;

    public ExampleRepositoryImpl(BigQueryAdminClient bqAdminClient) {
        this.bqAdminClient = bqAdminClient;
    }

    @Override
    public List<ResponseExample> findById(RequestExample request) {
        TableResult tableResult = bqAdminClient.queryById(request.getId());
        List<ResponseExample> queryResult = BigQueryUtil.toList(tableResult);
        return queryResult;
    }

    @Override
    public ResponseEntity<String> query(Query query) throws JsonProcessingException {
        ResponseEntity<String> response = bqAdminClient.restfulQuery(query);
        return response;
    }

    @Override
    public List<InsertAllRequest.RowToInsert> insert(List<RecordExample> records) {
        // TODO: Implement remainder of repository class insert() method.
        return Collections.emptyList();
    }

}
