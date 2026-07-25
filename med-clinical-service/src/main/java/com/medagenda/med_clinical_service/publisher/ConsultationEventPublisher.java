package com.medagenda.med_clinical_service.publisher;

import com.medagenda.med_clinical_service.config.RabbitMQConfig;
import com.medagenda.med_clinical_service.events.ConsultationFinishedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConsultationEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ConsultationEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishConsultationFinished(ConsultationFinishedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CLINICAL_EXCHANGE,
                "consultation.finished.key",
                event
        );
    }
}