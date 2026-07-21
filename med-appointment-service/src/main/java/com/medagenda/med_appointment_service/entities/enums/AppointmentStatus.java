package com.medagenda.med_appointment_service.entities.enums;

public enum AppointmentStatus {
    SCHEDULED,   // Appointment scheduled
    WAITING,     // The patient checked in at the reception desk
    IN_PROGRESS, // During the medical consultation
    FINISHED,    // Consultation completed
    CANCELED     // Appointment cancelled
}