package com.medagenda.med_document_service.dtos;

import com.medagenda.med_document_service.events.ClinicalNoteDTO;

import java.time.LocalDateTime;
import java.util.List;

public record ConsultationFinishedEvent(
        Long appointmentId,
        Long patientId,
        Long doctorId,
        LocalDateTime finishedAt,
        String symptoms,
        String diagnosis,
        String internalNotes,
        List<PrescriptionDTO> prescriptions,
        List<CertificateDTO> certificates
) {
}