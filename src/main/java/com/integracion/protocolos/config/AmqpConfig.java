package com.integracion.protocolos.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AmqpConfig {

    public static final String EXCHANGE_PRINCIPAL = "exchange.principal";
    public static final String EXCHANGE_DLQ = "exchange.dlq";
    public static final String QUEUE_PROCESAMIENTO = "cola.procesamiento";
    public static final String QUEUE_DLQ = "cola.dlq";
    public static final String ROUTING_KEY_PRINCIPAL = "routing.mensaje.#";
    public static final String ROUTING_KEY_DLQ = "routing.dlq";
    public static final String BINDING_PATTERN = "*.mensaje.*";

    @Bean
    public TopicExchange exchangePrincipal() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "topic");
        return ExchangeBuilder.topicExchange(EXCHANGE_PRINCIPAL)
                .durable(true)
                .autoDelete(false)
                .withArguments(arguments)
                .build();
    }

    @Bean
    public DirectExchange exchangeDlq() {
        return ExchangeBuilder.directExchange(EXCHANGE_DLQ)
                .durable(true)
                .autoDelete(false)
                .build();
    }

    @Bean
    public Queue colaProcesamiento() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", EXCHANGE_DLQ);
        arguments.put("x-dead-letter-routing-key", ROUTING_KEY_DLQ);
        arguments.put("x-message-ttl", 300000);
        arguments.put("x-max-length", 10000);
        arguments.put("x-overflow", "reject-publish");
        
        return QueueBuilder.durable(QUEUE_PROCESAMIENTO)
                .withArguments(arguments)
                .build();
    }

    @Bean
    public Queue colaDlq() {
        return QueueBuilder.durable(QUEUE_DLQ)
                .withArgument("x-message-ttl", 604800000)
                .build();
    }

    @Bean
    public Binding bindingProcesamiento(Queue colaProcesamiento, TopicExchange exchangePrincipal) {
        return BindingBuilder.bind(colaProcesamiento)
                .to(exchangePrincipal)
                .with(ROUTING_KEY_PRINCIPAL);
    }

    @Bean
    public Binding bindingDlq(Queue colaDlq, DirectExchange exchangeDlq) {
        return BindingBuilder.bind(colaDlq)
                .to(exchangeDlq)
                .with(ROUTING_KEY_DLQ);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setDefaultCharset("UTF-8");
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        template.setExchange(EXCHANGE_PRINCIPAL);
        template.setRoutingKey("mensaje.procesar");
        template.setRetryTemplate(retryTemplate());
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                System.err.println("Mensaje no confirmado: " + cause);
            }
        });
        template.setReturnsCallback(returned -> {
            System.err.println("Mensaje devuelto: " + returned.getMessage());
        });
        return template;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000);
        backOffPolicy.setMultiplier(2.0);
        backOffPolicy.setMaxInterval(10000);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setRetryableExceptions(new HashSet<>() {{
            add(Exception.class);
        }});
        retryTemplate.setRetryPolicy(retryPolicy);
        
        return retryTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, 
            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setPrefetchCount(25);
        factory.setDefaultRequeueRejected(false);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }
}