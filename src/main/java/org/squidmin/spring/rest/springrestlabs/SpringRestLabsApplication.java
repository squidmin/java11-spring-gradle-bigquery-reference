package org.squidmin.spring.rest.springrestlabs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class SpringRestLabsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestLabsApplication.class, args);
	}

}
