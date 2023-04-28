package org.squidmin.java.spring.gradle.bigquery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.squidmin.java.spring.gradle.bigquery.validation.FieldAConstraint;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestExample {

    private String id;

    @FieldAConstraint
    private String fieldA;

    private String fieldB;

    private String fieldC;

    private String fieldD;

}
