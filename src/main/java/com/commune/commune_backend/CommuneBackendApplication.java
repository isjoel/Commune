package com.commune.commune_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CommuneBackendApplication {

	public static void main(String[] args) {
        SpringApplication.run(CommuneBackendApplication.class, args);
	}
}

