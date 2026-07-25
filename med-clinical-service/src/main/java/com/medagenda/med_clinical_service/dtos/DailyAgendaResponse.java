package com.medagenda.med_clinical_service.dtos;

import java.time.LocalDateTime;

public record DailyAgendaResponse(
        Long appointmentId,
        Long patientId,
        String patientName,
        LocalDateTime appointmentTime,
        String status
) {
}