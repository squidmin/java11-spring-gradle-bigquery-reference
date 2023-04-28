package org.squidmin.java.spring.gradle.bigquery.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.squidmin.java.spring.gradle.bigquery.dto.Query;
import org.squidmin.java.spring.gradle.bigquery.dto.RequestExample;
import org.squidmin.java.spring.gradle.bigquery.dto.ResponseExample;

import java.util.List;

public interface ExampleRepository {

    List<ResponseExample> findById(RequestExample request);

    ResponseEntity<String> query(Query query) throws JsonProcessingException;

}
