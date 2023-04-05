package org.squidmin.spring.rest.springrestlabs.repository;

import org.squidmin.spring.rest.springrestlabs.dto.ResponseExample;

import java.util.List;

public interface ExampleRepository {

    List<ResponseExample> findById(String id);

}
