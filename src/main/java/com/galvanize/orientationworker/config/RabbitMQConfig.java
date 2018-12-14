package com.galvanize.orientationworker.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

public class RabbitMQConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);


    @Value("${amqp.exchange.name}")
    String appExchangeName;
    @Value("${amqp.verify.binding.key}")
    String verifyBinding;
    @Value("${amqp.verify.queue}")
    String verifyQueue;
    @Value("${amqp.checkout.binding.key}")
    String checkoutBinding;
    @Value("${amqp.checkout.queue}")
    String checkoutQueue;


    @Bean
    public TopicExchange getAppExchange() {
        return new TopicExchange(appExchangeName);
    }

    @Bean
    public Queue verifyQueue() {
        return new Queue(verifyQueue);
    }

    @Bean
    public Binding verifyBinding() {
        return BindingBuilder.bind(verifyQueue()).to(getAppExchange()).with(verifyBinding);
    }

    @Bean
    public Queue checkoutQueue() {
        return new Queue(checkoutQueue);
    }

    @Bean
    public Binding checkoutBinding() {
        return BindingBuilder.bind(checkoutQueue()).to(getAppExchange()).with(checkoutBinding);
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

    @Bean
    ConnectionFactory connectionFactory(String amqpUri) {
        LOGGER.info("Create Connection factory Bean with owm AMQP URI: {}", amqpUri);
        final CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri(amqpUri);
        return factory;
    }

//    @Override
//    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
//        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
//    }

//    @Bean
//    String amqpUri() {
//        LOGGER.info("Try to get amqpUri from appGateway: {}", appGateway);
//        RestTemplate rt = new RestTemplate();
//        return rt.getForEntity(appGateway + "/amqp.url", String.class).getBody();
//    }


}
