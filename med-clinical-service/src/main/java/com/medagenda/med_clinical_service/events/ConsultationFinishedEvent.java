package com.medagenda.med_clinical_service.events;

import java.time.LocalDateTime;

public record ConsultationFinishedEvent(
        Long appointmentId,
        Long patientId,
        Long doctorId,
        LocalDateTime finishedAt
) {
}