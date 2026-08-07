package com.medagenda.med_clinical_service.dtos;

import java.util.List;

public record FinalizeConsultationRequest(
        Long patientId,
        String symptoms,
        String diagnosis,
        String internalNotes,
        List<PrescriptionDTO> prescriptions,
        List<CertificateDTO> certificates
) {
}