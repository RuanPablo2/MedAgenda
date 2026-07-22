package com.medagenda.med_appointment_service.repositories;

import com.medagenda.med_appointment_service.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByScheduledAtBetween(LocalDateTime start, LocalDateTime end);
}