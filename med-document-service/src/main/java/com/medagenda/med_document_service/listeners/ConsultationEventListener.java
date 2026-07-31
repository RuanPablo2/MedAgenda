package com.medagenda.med_document_service.listeners;

import com.medagenda.med_document_service.config.RabbitMQConfig;
import com.medagenda.med_document_service.dtos.ConsultationFinishedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class ConsultationEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationEventListener.class);

    public ConsultationEventListener() {
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleConsultationFinished(ConsultationFinishedEvent event) {

        logger.info("Received consultation finished event for Appointment ID: {}", event.appointmentId());

        try {
            logger.info("Document generation process started for Appointment ID: {}", event.appointmentId());

        } catch (Exception e) {
            logger.error("Error processing document for Appointment ID: {}", event.appointmentId(), e);
            throw e;
        }
    }
}