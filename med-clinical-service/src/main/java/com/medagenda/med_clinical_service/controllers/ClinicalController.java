package com.medagenda.med_clinical_service.controllers;

import com.medagenda.med_clinical_service.dtos.FinalizeConsultationRequest;
import com.medagenda.med_clinical_service.entities.ClinicalHistory;
import com.medagenda.med_clinical_service.services.ClinicalHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clinical")
public class ClinicalController {

    private final ClinicalHistoryService service;

    public ClinicalController(ClinicalHistoryService service) {
        this.service = service;
    }

    @GetMapping("/patients/{patientId}/history")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<ClinicalHistory>> getPatientHistory(@PathVariable Long patientId) {
        List<ClinicalHistory> history = service.getPatientHistory(patientId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/appointments/{id}/finalize")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> finalizeConsultation(
            @PathVariable("id") Long appointmentId,
            @RequestHeader("X-User-Id") Long doctorId,
            @RequestBody FinalizeConsultationRequest request) {

        service.finalizeConsultation(appointmentId, doctorId, request);
        return ResponseEntity.noContent().build();
    }
}