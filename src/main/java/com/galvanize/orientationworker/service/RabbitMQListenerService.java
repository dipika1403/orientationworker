package com.galvanize.orientationworker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RabbitMQListenerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQListenerService.class);
    private final OrientationworkerService orientationworkerService;

    @Autowired
    public RabbitMQListenerService(OrientationworkerService orientationworkerService) {
        this.orientationworkerService = orientationworkerService;
    }

    @RabbitListener(queues = "${amqp.verify.queue}")
    public void verifyListener(String msg) {
        LOGGER.info("Received verify message: {} from pdf-worker exchange topic.", msg);

    }

    @RabbitListener(queues = "${amqp.checkout.queue}")
    public void checkoutListener(String msg) {
        LOGGER.info("Received checkout message: {} from pdf-worker exchange topic.", msg);
    }
}
