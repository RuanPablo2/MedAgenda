package com.medagenda.med_appointment_service.dtos;

import java.time.LocalDate;

public record PatientRequestDTO(
        String fullName,
        String cpf,
        String phone,
        LocalDate birthDate
) {}