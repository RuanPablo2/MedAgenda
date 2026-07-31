package com.medagenda.med_document_service.repositories;

import com.medagenda.med_document_service.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByAppointmentId(Long appointmentId);

}