package com.medagenda.med_appointment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.medagenda")
@EnableFeignClients
public class MedAppointmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedAppointmentServiceApplication.class, args);
	}

}
