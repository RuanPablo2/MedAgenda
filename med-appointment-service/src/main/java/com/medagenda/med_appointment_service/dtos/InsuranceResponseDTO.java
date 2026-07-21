package com.medagenda.med_appointment_service.dtos;

public record InsuranceResponseDTO(
        Long id,
        String name,
        boolean active
) {}