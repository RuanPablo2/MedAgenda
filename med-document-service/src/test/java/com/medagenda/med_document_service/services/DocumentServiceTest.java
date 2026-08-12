package com.medagenda.med_document_service.services;

import com.medagenda.med_commom.exceptions.ResourceNotFoundException;
import com.medagenda.med_document_service.entities.Document;
import com.medagenda.med_document_service.entities.enums.DocumentType;
import com.medagenda.med_document_service.repositories.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository repository;

    @InjectMocks
    private DocumentService documentService;

    private Document mockDocument;

    @BeforeEach
    void setUp() {
        mockDocument = new Document();
        mockDocument.setId(1L);
        mockDocument.setAppointmentId(10L);
        mockDocument.setDocumentType(DocumentType.PRESCRIPTION);
        mockDocument.setFileData(new byte[]{1, 2, 3});
    }

    @Test
    @DisplayName("Should successfully return a list of documents by appointment ID")
    void findAllByAppointmentId_Success() {
        when(repository.findByAppointmentId(10L)).thenReturn(List.of(mockDocument));

        List<Document> result = documentService.findAllByAppointmentId(10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(DocumentType.PRESCRIPTION, result.get(0).getDocumentType());
        verify(repository, times(1)).findByAppointmentId(10L);
    }

    @Test
    @DisplayName("Should successfully return a specific document by appointment ID and Type")
    void findByAppointmentAndType_Success() {
        when(repository.findByAppointmentIdAndDocumentType(10L, DocumentType.PRESCRIPTION))
                .thenReturn(Optional.of(mockDocument));

        Document result = documentService.findByAppointmentAndType(10L, DocumentType.PRESCRIPTION);

        assertNotNull(result);
        assertEquals(10L, result.getAppointmentId());
        assertEquals(DocumentType.PRESCRIPTION, result.getDocumentType());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException DOC_001 when document is not found")
    void findByAppointmentAndType_ThrowsException_NotFound() {
        when(repository.findByAppointmentIdAndDocumentType(10L, DocumentType.CERTIFICATE))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            documentService.findByAppointmentAndType(10L, DocumentType.CERTIFICATE);
        });

        assertEquals("DOC_001", exception.getErrorCode());
        assertTrue(exception.getMessage().contains("Document of type CERTIFICATE not found"));
    }
}