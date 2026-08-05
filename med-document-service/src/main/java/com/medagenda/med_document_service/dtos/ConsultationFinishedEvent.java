package com.medagenda.med_document_service.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record ConsultationFinishedEvent(
        Long appointmentId,
        Long doctorId,
        Long patientId,
        LocalDateTime finishedAt,
        List<ClinicalNoteDto> clinicalNotes
) {
}