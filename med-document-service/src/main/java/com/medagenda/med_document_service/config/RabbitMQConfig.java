package com.medagenda.med_document_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "med.clinical.events";
    public static final String QUEUE_NAME = "document.consultation.finished.queue";
    public static final String ROUTING_KEY = "consultation.finished.key";

    @Bean
    public TopicExchange clinicalEventsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue documentQueue() {

        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding binding(Queue documentQueue, TopicExchange clinicalEventsExchange) {
        return BindingBuilder.bind(documentQueue).to(clinicalEventsExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}