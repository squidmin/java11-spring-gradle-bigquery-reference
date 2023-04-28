package org.squidmin.java.spring.gradle.bigquery.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

import javax.persistence.Entity;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordExample {

    @Id
//    @GeneratedValue
    private String id;
    private String fieldA;
    private String fieldB;
    private String fieldC;
    private String fieldD;

}
