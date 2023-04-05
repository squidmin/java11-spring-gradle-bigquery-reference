package org.squidmin.spring.rest.springrestlabs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseExample {

    private String id;
    private String fieldA;
    private String fieldB;

}
