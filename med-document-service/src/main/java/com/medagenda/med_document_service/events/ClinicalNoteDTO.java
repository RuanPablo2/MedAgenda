package com.medagenda.med_document_service.events;

public record ClinicalNoteDTO(
        String type,
        String content
) {
}