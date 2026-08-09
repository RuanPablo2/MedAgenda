package com.medagenda.med_document_service.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record ConsultationFinishedEvent(
        Long appointmentId,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        LocalDateTime finishedAt,
        String symptoms,
        String diagnosis,
        String internalNotes,
        List<PrescriptionDTO> prescriptions,
        List<CertificateDTO> certificates
) {
}