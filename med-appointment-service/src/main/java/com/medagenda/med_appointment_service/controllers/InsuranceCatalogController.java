package com.medagenda.med_appointment_service.controllers;

import com.medagenda.med_appointment_service.dtos.InsuranceRequestDTO;
import com.medagenda.med_appointment_service.dtos.InsuranceResponseDTO;
import com.medagenda.med_appointment_service.services.InsuranceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalog/insurances")
public class InsuranceCatalogController {

    private final InsuranceService insuranceService;

    public InsuranceCatalogController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<InsuranceResponseDTO> createInsurance(@RequestBody InsuranceRequestDTO data) {
        InsuranceResponseDTO response = insuranceService.createInsurance(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}