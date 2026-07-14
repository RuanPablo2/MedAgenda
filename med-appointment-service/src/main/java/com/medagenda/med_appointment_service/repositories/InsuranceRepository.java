package com.medagenda.med_appointment_service.repositories;

import com.medagenda.med_appointment_service.entities.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
}