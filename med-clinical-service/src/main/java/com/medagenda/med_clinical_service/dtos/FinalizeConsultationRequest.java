package com.medagenda.med_clinical_service.dtos;

import java.util.Map;

public record FinalizeConsultationRequest(
        Long patientId,
        Map<String, Object> clinicalNotes
) {
}