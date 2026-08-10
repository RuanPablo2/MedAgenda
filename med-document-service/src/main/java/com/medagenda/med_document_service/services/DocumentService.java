package com.medagenda.med_document_service.services;

import com.medagenda.med_commom.exceptions.ResourceNotFoundException;
import com.medagenda.med_document_service.entities.Document;
import com.medagenda.med_document_service.entities.enums.DocumentType;
import com.medagenda.med_document_service.repositories.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    public List<Document> findAllByAppointmentId(Long appointmentId) {
        return repository.findByAppointmentId(appointmentId);
    }

    public Document findByAppointmentAndType(Long appointmentId, DocumentType documentType) {
        return repository.findByAppointmentIdAndDocumentType(appointmentId, documentType)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Document of type " + documentType + " not found for appointment: " + appointmentId,
                        "DOC_001"
                ));
    }
}