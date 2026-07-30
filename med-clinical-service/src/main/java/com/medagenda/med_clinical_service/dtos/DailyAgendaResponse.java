package com.medagenda.med_clinical_service.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record DailyAgendaResponse(
        @JsonProperty("id")
        Long appointmentId,

        Long patientId,

        String patientName,

        @JsonProperty("scheduledAt")
        LocalDateTime appointmentTime,

        String status
) {}