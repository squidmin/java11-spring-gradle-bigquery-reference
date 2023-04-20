package org.squidmin.spring.rest.springrestlabs.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.squidmin.spring.rest.springrestlabs.dto.Query;
import org.squidmin.spring.rest.springrestlabs.dto.RequestExample;
import org.squidmin.spring.rest.springrestlabs.dto.ResponseExample;

import java.util.List;

public interface ExampleRepository {

    List<ResponseExample> findById(RequestExample request);

    ResponseEntity<String> query(Query query) throws JsonProcessingException;

}
