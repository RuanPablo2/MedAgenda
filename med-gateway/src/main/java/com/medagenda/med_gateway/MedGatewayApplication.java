package com.medagenda.med_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;

@SpringBootApplication(
		scanBasePackages = "com.medagenda",
		exclude = {SecurityAutoConfiguration.class}
)
public class MedGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedGatewayApplication.class, args);
	}

}
