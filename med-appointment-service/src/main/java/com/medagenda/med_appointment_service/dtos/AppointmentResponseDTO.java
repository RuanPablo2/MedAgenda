package com.medagenda.med_appointment_service.dtos;

import com.medagenda.med_appointment_service.entities.enums.AppointmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppointmentResponseDTO(
        Long id,
        Long doctorId,
        String patientName,
        String insuranceName,
        BigDecimal price,
        LocalDateTime scheduledAt,
        AppointmentStatus status
) {}