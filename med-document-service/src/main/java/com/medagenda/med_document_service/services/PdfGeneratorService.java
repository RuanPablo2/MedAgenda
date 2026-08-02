package com.medagenda.med_document_service.services;

import com.medagenda.med_document_service.dtos.ConsultationFinishedEvent;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Service
public class PdfGeneratorService {

    private final TemplateEngine templateEngine;

    public PdfGeneratorService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generateConsultationPdf(ConsultationFinishedEvent event) {

        Context context = new Context();
        context.setVariable("event", event);
        context.setVariable("appointmentId", event.appointmentId());

        String htmlContent = templateEngine.process("consultation-document", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate the consultation PDF.", e);
        }
    }
}