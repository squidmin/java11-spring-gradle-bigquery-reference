package org.squidmin.spring.rest.springrestlabs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.squidmin.spring.rest.springrestlabs.validation.RequestExampleConstraint;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestExample {

    private String id;

    @RequestExampleConstraint
    private String constrainedField;

}
