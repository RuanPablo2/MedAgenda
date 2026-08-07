package com.medagenda.med_document_service.dtos;

public record CertificateDTO(
        Integer days,
        String reason
) {
}