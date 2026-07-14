package com.medagenda.med_appointment_service.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppointmentRequestDTO(
        Long doctorId,
        Long patientId,
        Long insuranceId,
        BigDecimal price,
        LocalDateTime scheduledAt
) {}