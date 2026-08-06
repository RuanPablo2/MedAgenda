package com.medagenda.med_document_service.repositories;

import com.medagenda.med_document_service.entities.Document;
import com.medagenda.med_document_service.entities.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByAppointmentId(Long appointmentId);

    Optional<Document> findByAppointmentIdAndDocumentType(Long appointmentId, DocumentType documentType);
}