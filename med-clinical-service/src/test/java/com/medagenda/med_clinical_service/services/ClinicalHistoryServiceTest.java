package com.medagenda.med_clinical_service.services;

import com.medagenda.med_clinical_service.dtos.AppointmentDetailsDTO;
import com.medagenda.med_clinical_service.dtos.FinalizeConsultationRequest;
import com.medagenda.med_clinical_service.entities.ClinicalHistory;
import com.medagenda.med_clinical_service.events.ConsultationFinishedEvent;
import com.medagenda.med_clinical_service.integration.AppointmentClient;
import com.medagenda.med_clinical_service.publisher.ConsultationEventPublisher;
import com.medagenda.med_clinical_service.repositories.ClinicalHistoryRepository;
import com.medagenda.med_commom.exceptions.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicalHistoryServiceTest {

    @Mock
    private ClinicalHistoryRepository repository;

    @Mock
    private AppointmentClient appointmentClient;

    @Mock
    private ConsultationEventPublisher eventPublisher;

    @InjectMocks
    private ClinicalHistoryService clinicalHistoryService;

    @Test
    @DisplayName("Should successfully retrieve patient clinical history in descending order")
    void getPatientHistory_Success() {
        ClinicalHistory fakeHistory = new ClinicalHistory();

        when(repository.findByPatientIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(fakeHistory));

        List<ClinicalHistory> result = clinicalHistoryService.getPatientHistory(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByPatientIdOrderByCreatedAtDesc(1L);
    }

    @Test
    @DisplayName("Should finalize consultation, save history, update HTTP status and publish RabbitMQ event")
    void finalizeConsultation_Success() {
        Long appointmentId = 10L;
        Long doctorId = 2L;
        Long patientId = 1L;

        FinalizeConsultationRequest mockRequest = mock(FinalizeConsultationRequest.class);
        when(mockRequest.patientId()).thenReturn(patientId);

        AppointmentDetailsDTO mockDetails = mock(AppointmentDetailsDTO.class);
        when(mockDetails.patientId()).thenReturn(patientId);

        when(appointmentClient.getAppointmentById(appointmentId, doctorId)).thenReturn(mockDetails);

        clinicalHistoryService.finalizeConsultation(appointmentId, doctorId, mockRequest);

        verify(repository, times(1)).save(any(ClinicalHistory.class));

        verify(appointmentClient, times(1)).updateAppointmentStatus(appointmentId, "FINISHED", doctorId);

        verify(eventPublisher, times(1)).publishConsultationFinished(any(ConsultationFinishedEvent.class));
    }

    @Test
    @DisplayName("Should throw BusinessException CLIN_001 and rollback when patient ID from payload is divergent")
    void finalizeConsultation_ThrowsException_PatientIdMismatch() {
        Long appointmentId = 10L;
        Long doctorId = 2L;

        FinalizeConsultationRequest mockRequest = mock(FinalizeConsultationRequest.class);
        when(mockRequest.patientId()).thenReturn(99L);

        AppointmentDetailsDTO mockDetails = mock(AppointmentDetailsDTO.class);
        when(mockDetails.patientId()).thenReturn(1L);

        when(appointmentClient.getAppointmentById(appointmentId, doctorId)).thenReturn(mockDetails);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            clinicalHistoryService.finalizeConsultation(appointmentId, doctorId, mockRequest);
        });

        assertEquals("CLIN_001", exception.getErrorCode());

        verify(repository, never()).save(any(ClinicalHistory.class));
        verify(appointmentClient, never()).updateAppointmentStatus(anyLong(), anyString(), anyLong());
        verify(eventPublisher, never()).publishConsultationFinished(any(ConsultationFinishedEvent.class));
    }
}