package com.medagenda.med_appointment_service.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppointmentResponseDTO(
        Long id,
        Long doctorId,
        String patientName,
        String insuranceName,
        BigDecimal price,
        LocalDateTime scheduledAt,
        String status
) {}