package com.medagenda.med_document_service.listeners;

import com.medagenda.med_document_service.config.RabbitMQConfig;
import com.medagenda.med_document_service.dtos.ConsultationFinishedEvent;
import com.medagenda.med_document_service.entities.Document;
import com.medagenda.med_document_service.entities.enums.DocumentType;
import com.medagenda.med_document_service.events.ClinicalNoteDTO;
import com.medagenda.med_document_service.repositories.DocumentRepository;
import com.medagenda.med_document_service.services.PdfGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import java.util.List;

@Component
public class ConsultationEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationEventListener.class);

    private final PdfGeneratorService pdfGeneratorService;
    private final DocumentRepository documentRepository;

    public ConsultationEventListener(PdfGeneratorService pdfGeneratorService, DocumentRepository documentRepository) {
        this.pdfGeneratorService = pdfGeneratorService;
        this.documentRepository = documentRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleConsultationFinished(ConsultationFinishedEvent event) {
        logger.info("Starting document generation for Appointment ID: {}", event.appointmentId());

        try {
            Context recordContext = new Context();
            recordContext.setVariable("event", event);
            recordContext.setVariable("appointmentId", event.appointmentId());
            byte[] recordBytes = pdfGeneratorService.generatePdf("medical-record", recordContext);

            documentRepository.save(new Document(event.appointmentId(), DocumentType.RECORD, recordBytes));
            logger.info("Generated RECORD for Appointment ID: {}", event.appointmentId());

            List<ClinicalNoteDTO> prescription = event.clinicalNotes().stream()
                    .filter(note -> note.type().equalsIgnoreCase("prescricao") || note.type().equalsIgnoreCase("prescription"))
                    .toList();

            if (!prescription.isEmpty()) {
                Context prescriptionContext = new Context();
                prescriptionContext.setVariable("event", event);
                prescriptionContext.setVariable("prescription", prescription);

                byte[] prescriptionBytes = pdfGeneratorService.generatePdf("prescription", prescriptionContext);

                documentRepository.save(new Document(event.appointmentId(), DocumentType.PRESCRIPTION, prescriptionBytes));
                logger.info("Generated PRESCRIPTION for Appointment ID: {}", event.appointmentId());
            }

            List<ClinicalNoteDTO> certificate = event.clinicalNotes().stream()
                    .filter(note -> note.type().equalsIgnoreCase("atestado") || note.type().equalsIgnoreCase("certificate"))
                    .toList();

            if (!certificate.isEmpty()) {
                Context certificateContext = new Context();
                certificateContext.setVariable("event", event);
                certificateContext.setVariable("certificate", certificate);

                byte[] certificateBytes = pdfGeneratorService.generatePdf("medical-certificate", certificateContext);

                documentRepository.save(new Document(event.appointmentId(), DocumentType.CERTIFICATE, certificateBytes));
                logger.info("Generated CERTIFICATE for Appointment ID: {}", event.appointmentId());
            }

            logger.info("Successfully processed all documents for Appointment ID: {}", event.appointmentId());

        } catch (Exception e) {
            logger.error("Error processing documents for Appointment ID: {}", event.appointmentId(), e);
            throw e;
        }
    }
}