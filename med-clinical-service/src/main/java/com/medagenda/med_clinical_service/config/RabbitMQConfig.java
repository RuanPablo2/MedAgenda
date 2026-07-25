package com.medagenda.med_clinical_service.config;

import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CLINICAL_EXCHANGE = "clinical.topic";

    @Bean
    public TopicExchange clinicalExchange() {
        return ExchangeBuilder.topicExchange(CLINICAL_EXCHANGE).durable(true).build();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}