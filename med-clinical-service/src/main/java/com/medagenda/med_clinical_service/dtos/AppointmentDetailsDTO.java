package com.medagenda.med_clinical_service.dtos;

public record AppointmentDetailsDTO(
        Long id,
        Long doctorId,
        String doctorName,
        Long patientId,
        String patientName
) {}