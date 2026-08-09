package com.medagenda.med_document_service.listeners;

import com.medagenda.med_document_service.config.RabbitMQConfig;
import com.medagenda.med_document_service.dtos.ConsultationFinishedEvent;
import com.medagenda.med_document_service.entities.Document;
import com.medagenda.med_document_service.entities.enums.DocumentType;
import com.medagenda.med_document_service.repositories.DocumentRepository;
import com.medagenda.med_document_service.services.PdfGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

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
    @Transactional
    public void handleConsultationFinished(ConsultationFinishedEvent event) {
        logger.info("Starting document generation for Appointment ID: {}", event.appointmentId());

        try {
            Context recordContext = new Context();
            recordContext.setVariable("event", event);
            byte[] recordBytes = pdfGeneratorService.generatePdf("medical-record", recordContext);
            documentRepository.save(new Document(event.appointmentId(), DocumentType.RECORD, recordBytes));
            logger.info("Generated RECORD for Appointment ID: {}", event.appointmentId());

            if (event.prescriptions() != null && !event.prescriptions().isEmpty()) {
                Context prescriptionContext = new Context();
                prescriptionContext.setVariable("event", event);
                prescriptionContext.setVariable("prescriptions", event.prescriptions());

                byte[] prescriptionBytes = pdfGeneratorService.generatePdf("prescription", prescriptionContext);
                documentRepository.save(new Document(event.appointmentId(), DocumentType.PRESCRIPTION, prescriptionBytes));
                logger.info("Generated PRESCRIPTION for Appointment ID: {}", event.appointmentId());
            }

            if (event.certificates() != null && !event.certificates().isEmpty()) {
                Context certificateContext = new Context();
                certificateContext.setVariable("event", event);
                certificateContext.setVariable("certificates", event.certificates());

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