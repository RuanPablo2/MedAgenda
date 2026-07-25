package com.medagenda.med_clinical_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.medagenda.med_clinical_service.dtos.FinalizeConsultationRequest;
import com.medagenda.med_clinical_service.entities.ClinicalHistory;
import com.medagenda.med_clinical_service.integration.AppointmentClient;
import com.medagenda.med_clinical_service.repositories.ClinicalHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClinicalHistoryService {

    private final ClinicalHistoryRepository repository;
    private final ObjectMapper objectMapper;
    private final AppointmentClient appointmentClient;

    public ClinicalHistoryService(ClinicalHistoryRepository repository,
                                  ObjectMapper objectMapper,
                                  AppointmentClient appointmentClient) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.appointmentClient = appointmentClient;
    }

    public List<ClinicalHistory> getPatientHistory(Long patientId) {
        return repository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    @Transactional
    public void finalizeConsultation(Long appointmentId, Long doctorId, FinalizeConsultationRequest request) {
        try {
            String jsonbNotes = objectMapper.writeValueAsString(request.clinicalNotes());

            ClinicalHistory history = new ClinicalHistory(
                    appointmentId,
                    request.patientId(),
                    doctorId,
                    LocalDateTime.now(),
                    jsonbNotes
            );
            repository.save(history);

            appointmentClient.updateAppointmentStatus(appointmentId, "FINISHED");


        } catch (Exception e) {
            throw new RuntimeException("CLINICAL_001: Error processing clinical notes JSON format", e);
        }
    }
}