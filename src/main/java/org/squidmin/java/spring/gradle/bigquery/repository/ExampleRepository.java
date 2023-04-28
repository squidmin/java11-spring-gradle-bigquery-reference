package org.squidmin.java.spring.gradle.bigquery.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.squidmin.java.spring.gradle.bigquery.dao.RecordExample;
import org.squidmin.java.spring.gradle.bigquery.dto.Query;
import org.squidmin.java.spring.gradle.bigquery.dto.ResponseExample;

import java.util.List;

public interface ExampleRepository {

    List<ResponseExample> findById(String id);

    ResponseExample query(Query query) throws JsonProcessingException;

    int insert(List<RecordExample> records);

}
