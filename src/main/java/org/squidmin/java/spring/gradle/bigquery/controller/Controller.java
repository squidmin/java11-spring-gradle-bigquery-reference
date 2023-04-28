package org.squidmin.java.spring.gradle.bigquery.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.squidmin.java.spring.gradle.bigquery.dao.RecordExample;
import org.squidmin.java.spring.gradle.bigquery.dto.Query;
import org.squidmin.java.spring.gradle.bigquery.dto.RequestExample;
import org.squidmin.java.spring.gradle.bigquery.dto.ResponseExample;
import org.squidmin.java.spring.gradle.bigquery.repository.ExampleRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;

@RestController(value = "/bigquery")
public class Controller {

    private final ExampleRepositoryImpl repository;

    public Controller(ExampleRepositoryImpl repository) {
        this.repository = repository;
    }

    @GetMapping(
        value = "/find-by-id",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<ResponseExample>> findById(@RequestParam String id) {
        List<ResponseExample> response = repository.findById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(
        value = "/query",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseExample> query(@RequestBody Query query) throws JsonProcessingException {
        ResponseExample response = repository.query(query);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(
        value = "/insert",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> insert(@RequestBody List<RequestExample> request) {
        return new ResponseEntity<>(0 < repository.insert(mapToDao(request)) ? HttpStatus.OK : HttpStatus.ACCEPTED);
    }

    private List<RecordExample> mapToDao(List<RequestExample> request) {
        return request.stream().map(r -> RecordExample.builder()
            .id(r.getId())
            .fieldA(r.getFieldA())
            .fieldB(r.getFieldB())
            .fieldC(r.getFieldC())
            .fieldD(r.getFieldD())
            .build()
        ).collect(Collectors.toList());
    }

}
