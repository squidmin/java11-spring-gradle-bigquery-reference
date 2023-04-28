package org.squidmin.java.spring.gradle.bigquery.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.cloud.bigquery.InsertAllRequest;
import org.springframework.http.ResponseEntity;
import org.squidmin.java.spring.gradle.bigquery.dao.RecordExample;
import org.squidmin.java.spring.gradle.bigquery.dto.Query;
import org.squidmin.java.spring.gradle.bigquery.dto.ResponseExample;

import java.util.List;

public interface ExampleRepository {

    List<ResponseExample> findById(String id);

    ResponseEntity<String> query(Query query) throws JsonProcessingException;

    List<InsertAllRequest.RowToInsert> insert(List<RecordExample> records);

}
