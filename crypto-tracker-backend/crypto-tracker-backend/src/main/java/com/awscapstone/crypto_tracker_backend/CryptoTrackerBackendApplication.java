package com.awscapstone.crypto_tracker_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoTrackerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoTrackerBackendApplication.class, args);
	}

}
