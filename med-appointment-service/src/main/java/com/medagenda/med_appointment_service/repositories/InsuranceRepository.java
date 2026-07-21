package com.medagenda.med_appointment_service.repositories;

import com.medagenda.med_appointment_service.entities.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    Optional<Insurance> findByNameIgnoreCase(String name);
}