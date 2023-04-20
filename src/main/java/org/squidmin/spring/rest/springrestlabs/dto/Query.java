package org.squidmin.spring.rest.springrestlabs.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Query {

    private String query;
    private String useLegacySql;

}
