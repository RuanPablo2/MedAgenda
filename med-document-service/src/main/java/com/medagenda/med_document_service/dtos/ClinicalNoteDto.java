package com.medagenda.med_document_service.dtos;

public record ClinicalNoteDto(
        String type,
        String content,
        String metadata
) {
}