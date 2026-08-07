package com.medagenda.med_clinical_service.dtos;

import java.util.List;

public record PrescriptionDTO(
        String type, // Ex: "COMMON", "SPECIAL_CONTROL"
        List<String> medications
) {
}