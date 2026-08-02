package com.medagenda.med_document_service.services;

import com.medagenda.med_document_service.entities.Document;
import com.medagenda.med_document_service.repositories.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    public Document findByAppointmentId(Long appointmentId) {
        return repository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Document not found for appointment: " + appointmentId));
    }
}