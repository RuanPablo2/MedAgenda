package com.medagenda.med_appointment_service.controllers;

import com.medagenda.med_appointment_service.dtos.PatientRequestDTO;
import com.medagenda.med_appointment_service.dtos.PatientResponseDTO;
import com.medagenda.med_appointment_service.services.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PreAuthorize("hasRole('RECEPTION')")
    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@RequestBody PatientRequestDTO data) {
        PatientResponseDTO response = patientService.createPatient(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}