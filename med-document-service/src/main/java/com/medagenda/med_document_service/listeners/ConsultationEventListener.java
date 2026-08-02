package com.medagenda.med_document_service.listeners;

import com.medagenda.med_document_service.config.RabbitMQConfig;
import com.medagenda.med_document_service.dtos.ConsultationFinishedEvent;
import com.medagenda.med_document_service.entities.Document;
import com.medagenda.med_document_service.repositories.DocumentRepository;
import com.medagenda.med_document_service.services.PdfGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


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

        logger.info("Received consultation finished event for Appointment ID: {}", event.appointmentId());

        try {
            logger.info("Document generation process started for Appointment ID: {}", event.appointmentId());

            byte[] pdfBytes = pdfGeneratorService.generateConsultationPdf(event);

            Document document = new Document(event.appointmentId(), pdfBytes);

            documentRepository.save(document);

            logger.info("Successfully generated and saved PDF for Appointment ID: {}", event.appointmentId());

        } catch (Exception e) {
            logger.error("Error processing document for Appointment ID: {}", event.appointmentId(), e);
            throw e;
        }
    }
}