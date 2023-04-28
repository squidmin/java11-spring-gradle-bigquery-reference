package org.squidmin.java.spring.gradle.bigquery.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity(name = "RecordExample")
//@Table(name = "test_table_name_local")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordExample {

//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
//    @Column(name="fieldA", length=50, nullable=false, unique=false)
    private String fieldA;
    private String fieldB;
    private String fieldC;
    private String fieldD;
    private String fieldE;
    private String fieldF;

}
