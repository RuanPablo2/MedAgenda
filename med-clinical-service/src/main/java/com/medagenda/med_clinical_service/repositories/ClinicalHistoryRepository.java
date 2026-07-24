package com.medagenda.med_clinical_service.repositories;

import com.medagenda.med_clinical_service.entities.ClinicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClinicalHistoryRepository extends JpaRepository<ClinicalHistory, Long> {

    List<ClinicalHistory> findByPatientIdOrderByCreatedAtDesc(Long patientId);

}