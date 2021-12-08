package com.kosh.csvrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CsvRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsvRestApiApplication.class, args);
	}

}
