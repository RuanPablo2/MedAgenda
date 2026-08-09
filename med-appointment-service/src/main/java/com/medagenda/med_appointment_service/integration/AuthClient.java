package com.medagenda.med_appointment_service.integration;

import com.medagenda.med_appointment_service.dtos.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "med-auth-service", url = "http://localhost:8081")
public interface AuthClient {

    @GetMapping("/api/v1/auth/users/{id}")
    UserResponseDTO getUserById(@PathVariable("id") Long id);
}