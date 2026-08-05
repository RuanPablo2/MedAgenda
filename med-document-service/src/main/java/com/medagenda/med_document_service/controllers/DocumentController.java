package com.medagenda.med_document_service.controllers;

import com.medagenda.med_document_service.entities.Document;
import com.medagenda.med_document_service.services.DocumentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<byte[]> getDocumentByAppointment(@PathVariable Long appointmentId) {

        Document document = documentService.findByAppointmentId(appointmentId);

        return ResponseEntity.ok()

                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"prontuario-" + appointmentId + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(document.getFileData());
    }
}