package com.medagenda.med_clinical_service.services;

import com.medagenda.med_clinical_service.dtos.AppointmentDetailsDTO;
import com.medagenda.med_clinical_service.dtos.FinalizeConsultationRequest;
import com.medagenda.med_clinical_service.entities.ClinicalHistory;
import com.medagenda.med_clinical_service.events.ConsultationFinishedEvent;
import com.medagenda.med_clinical_service.integration.AppointmentClient;
import com.medagenda.med_clinical_service.publisher.ConsultationEventPublisher;
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
    private final ConsultationEventPublisher eventPublisher;

    public ClinicalHistoryService(ClinicalHistoryRepository repository,
                                  ObjectMapper objectMapper,
                                  AppointmentClient appointmentClient,
                                  ConsultationEventPublisher eventPublisher) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.appointmentClient = appointmentClient;
        this.eventPublisher = eventPublisher;
    }

    public List<ClinicalHistory> getPatientHistory(Long patientId) {
        return repository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    @Transactional
    public void finalizeConsultation(Long appointmentId, Long doctorId, FinalizeConsultationRequest request) {

        AppointmentDetailsDTO appointmentDetails = appointmentClient.getAppointmentById(appointmentId, doctorId);

        ClinicalHistory history = new ClinicalHistory(
                appointmentId,
                request.patientId(),
                doctorId,
                LocalDateTime.now(),
                request.symptoms(),
                request.diagnosis(),
                request.internalNotes(),
                request.prescriptions(),
                request.certificates()
        );

        repository.save(history);

        appointmentClient.updateAppointmentStatus(appointmentId, "FINISHED", doctorId);

        ConsultationFinishedEvent event = new ConsultationFinishedEvent(
                appointmentId,
                request.patientId(),
                appointmentDetails.patientName(),
                doctorId,
                appointmentDetails.doctorName(),
                history.getCreatedAt(),
                request.symptoms(),
                request.diagnosis(),
                request.internalNotes(),
                request.prescriptions(),
                request.certificates()
        );

        eventPublisher.publishConsultationFinished(event);
    }
}