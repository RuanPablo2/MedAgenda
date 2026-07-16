package com.medagenda.med_auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.medagenda")
public class MedAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedAuthServiceApplication.class, args);
	}

}
