package com.medagenda.med_clinical_service.events;

import java.time.LocalDateTime;
import java.util.List;

public record ConsultationFinishedEvent(
        Long appointmentId,
        Long patientId,
        Long doctorId,
        LocalDateTime finishedAt,
        List<ClinicalNoteDTO> clinicalNotes
) {}