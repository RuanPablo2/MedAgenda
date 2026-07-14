package com.medagenda.med_appointment_service.repositories;

import com.medagenda.med_appointment_service.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}