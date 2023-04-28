package org.squidmin.java.spring.gradle.bigquery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.squidmin.java.spring.gradle.bigquery.validation.RequestExampleConstraint;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestExample {

    private String id;

    @RequestExampleConstraint
    private String constrainedField;

}
