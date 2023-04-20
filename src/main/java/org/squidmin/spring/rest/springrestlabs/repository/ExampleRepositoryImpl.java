package org.squidmin.spring.rest.springrestlabs.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.cloud.bigquery.TableResult;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.squidmin.spring.rest.springrestlabs.dto.Query;
import org.squidmin.spring.rest.springrestlabs.dto.RequestExample;
import org.squidmin.spring.rest.springrestlabs.dto.ResponseExample;
import org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClient;
import org.squidmin.spring.rest.springrestlabs.util.BigQueryUtil;

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

}
