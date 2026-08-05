package com.medagenda.med_clinical_service.events;

public record ClinicalNoteDTO(
        String type,
        String content
) {
}