package org.squidmin.spring.rest.springrestlabs.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.squidmin.spring.rest.springrestlabs.dto.ResponseExample;
import org.squidmin.spring.rest.springrestlabs.repository.ExampleRepositoryImpl;

import java.util.List;

@RestController
public class Controller {

    private ExampleRepositoryImpl repository;

    Controller(ExampleRepositoryImpl repository) {
        this.repository = repository;
    }

    @GetMapping("/find-by-id")
    public ResponseEntity<List<ResponseExample>> findById(@RequestParam String id) {
        List<ResponseExample> response = repository.findById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
