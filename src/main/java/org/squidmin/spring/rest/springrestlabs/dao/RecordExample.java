package org.squidmin.spring.rest.springrestlabs.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordExample {

    @Id
    @GeneratedValue
    private Long id;
    private String fieldA;
    private String fieldB;
    private String fieldC;
    private String fieldD;

}
