package com.medagenda.med_document_service.dtos;

import java.util.List;

public record ConsultationFinishedEvent(
        Long appointmentId,
        Long doctorId,
        Long patientId,
        List<ClinicalNoteDto> clinicalNotes
) {
}