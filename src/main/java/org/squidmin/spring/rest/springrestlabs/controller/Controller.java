package org.squidmin.spring.rest.springrestlabs.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.squidmin.spring.rest.springrestlabs.dto.RequestExample;
import org.squidmin.spring.rest.springrestlabs.dto.ResponseExample;
import org.squidmin.spring.rest.springrestlabs.repository.ExampleRepositoryImpl;

import java.util.List;

@RestController
public class Controller {

    private final ExampleRepositoryImpl repository;

    public Controller(ExampleRepositoryImpl repository) {
        this.repository = repository;
    }

    @GetMapping("/find-by-id")
    public ResponseEntity<List<ResponseExample>> findById(@RequestBody RequestExample request) {
        List<ResponseExample> response = repository.findById(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
