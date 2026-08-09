package com.medagenda.med_appointment_service.dtos;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String role
) {}