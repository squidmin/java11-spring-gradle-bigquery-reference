package org.squidmin.java.spring.gradle.bigquery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class JavaSpringGradleBigQueryReferenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaSpringGradleBigQueryReferenceApplication.class, args);
	}

}
