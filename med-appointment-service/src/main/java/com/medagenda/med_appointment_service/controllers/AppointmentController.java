package com.medagenda.med_appointment_service.controllers;

import com.medagenda.med_appointment_service.dtos.AppointmentRequestDTO;
import com.medagenda.med_appointment_service.dtos.AppointmentResponseDTO;
import com.medagenda.med_appointment_service.dtos.AppointmentStatusUpdateDTO;
import com.medagenda.med_appointment_service.services.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PreAuthorize("hasRole('RECEPTION')")
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> scheduleAppointment(@RequestBody AppointmentRequestDTO data) {
        AppointmentResponseDTO response = appointmentService.scheduleAppointment(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole('RECEPTION', 'DOCTOR')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody AppointmentStatusUpdateDTO data) {

        appointmentService.updateAppointmentStatus(id, data);

        return ResponseEntity.noContent().build();
    }
}