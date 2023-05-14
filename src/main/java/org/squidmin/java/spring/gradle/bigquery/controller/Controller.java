package org.squidmin.java.spring.gradle.bigquery.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.squidmin.java.spring.gradle.bigquery.dao.RecordExample;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequest;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequestItem;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponse;
import org.squidmin.java.spring.gradle.bigquery.dto.Query;
import org.squidmin.java.spring.gradle.bigquery.repository.ExampleRepositoryImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController(value = "/bigquery")
public class Controller {

    private final ExampleRepositoryImpl repository;

    public Controller(ExampleRepositoryImpl repository) {
        this.repository = repository;
    }

    @GetMapping(
        value = "/query",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ExampleResponse> query(@Valid @RequestBody Query query) {
        return new ResponseEntity<>(repository.query(query), HttpStatus.OK);
    }

    @PostMapping(
        value = "/insert",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> insert(@Valid @RequestBody ExampleRequest request) {
        return new ResponseEntity<>(
            0 < repository.insert(
                request.getProjectId(),
                request.getDataset(),
                request.getTable(),
                mapToDao(request.getBody())
            ) ? HttpStatus.OK : HttpStatus.ACCEPTED
        );
    }

    private List<RecordExample> mapToDao(List<ExampleRequestItem> request) {
        return request.stream().map(r -> RecordExample.builder()
            .id(r.getId())
            .creationTimestamp(r.getCreationTimestamp())
            .lastUpdateTimestamp(r.getLastUpdateTimestamp())
            .columnA(r.getColumnA())
            .columnB(r.getColumnB())
            .build()
        ).collect(Collectors.toList());
    }

}
