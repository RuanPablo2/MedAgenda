package com.medagenda.med_document_service.dtos;

import java.util.List;

public record PrescriptionDTO(
        String type,
        List<String> medications
) {
}