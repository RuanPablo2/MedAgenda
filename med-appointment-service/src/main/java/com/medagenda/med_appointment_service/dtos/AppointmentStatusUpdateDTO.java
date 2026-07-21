package com.medagenda.med_appointment_service.dtos;

import com.medagenda.med_appointment_service.entities.enums.AppointmentStatus;

public record AppointmentStatusUpdateDTO(
        AppointmentStatus status
) {}