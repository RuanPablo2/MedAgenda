package com.medagenda.med_document_service.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PdfGeneratorServiceTest {

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private PdfGeneratorService pdfGeneratorService;

    @Test
    @DisplayName("Should successfully generate a PDF byte array from an HTML template")
    void generatePdf_Success() {
        Context mockContext = new Context();
        String validHtml = "<html><head><title>Test</title></head><body><p>Mocked PDF</p></body></html>";

        when(templateEngine.process(eq("prescription"), any(Context.class))).thenReturn(validHtml);

        byte[] pdfBytes = pdfGeneratorService.generatePdf("prescription", mockContext);

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        verify(templateEngine, times(1)).process(eq("prescription"), any(Context.class));
    }

    @Test
    @DisplayName("Should throw a generic RuntimeException when PDF generation fails due to bad HTML")
    void generatePdf_ThrowsException_OnFailure() {
        Context mockContext = new Context();

        String invalidHtml = "<html><body><p>Teste</p><br></body></html>";

        when(templateEngine.process(eq("certificate"), any(Context.class))).thenReturn(invalidHtml);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pdfGeneratorService.generatePdf("certificate", mockContext);
        });

        assertTrue(exception.getMessage().contains("Failed to generate PDF for template: certificate"));
    }
}