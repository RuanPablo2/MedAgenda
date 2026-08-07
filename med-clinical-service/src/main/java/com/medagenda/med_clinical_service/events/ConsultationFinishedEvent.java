package com.medagenda.med_clinical_service.events;

import com.medagenda.med_clinical_service.dtos.CertificateDTO;
import com.medagenda.med_clinical_service.dtos.PrescriptionDTO;

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