package com.integracion.protocolos.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integracion.protocolos.domain.MessageModel;
import com.integracion.protocolos.domain.MessageModel.MessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class AmqpProducer {

    private static final Logger log = LoggerFactory.getLogger(AmqpProducer.class);
    private static final String DEFAULT_EXCHANGE = "protocolos.exchange";
    private static final String MAIN_QUEUE = "protocolos.main.queue";
    private static final String DLQ_QUEUE = "protocolos.dlq.queue";
    private static final String MAIN_ROUTING_KEY = "protocolos.message";
    private static final String DLQ_ROUTING_KEY = "protocolos.dlq";
    private static final int MAX_RETRIES = 3;

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final Sinks.Many<MessageModel> messageSink;
    private final AtomicInteger messageCounter;

    @Value("${amqp.producer.retry.enabled:true}")
    private boolean retryEnabled;

    @Value("${amqp.producer.dlq.enabled:true}")
    private boolean dlqEnabled;

    public AmqpProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.messageSink = Sinks.many().unicast().onBackpressureBuffer();
        this.messageCounter = new AtomicInteger(0);
        configureMessageConverter();
    }

    private void configureMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setTypeIdPropertyName("__typeId__");
        rabbitTemplate.setMessageConverter(converter);
    }

    public Mono<MessageModel> sendMessage(MessageModel message) {
        return Mono.fromCallable(() -> {
            String correlationId = UUID.randomUUID().toString();
            MessageModel messageWithMeta = message.withStatus(MessageStatus.SENT);
            
            Map<String, Object> headers = new HashMap<>();
            headers.put("correlationId", correlationId);
            headers.put("timestamp", System.currentTimeMillis());
            headers.put("source", "amqp-producer");
            headers.put("retryCount", 0);
            headers.put("maxRetries", MAX_RETRIES);
            
            try {
                String jsonPayload = objectMapper.writeValueAsString(messageWithMeta);
                org.springframework.amqp.core.Message amqpMessage = createAmqpMessage(jsonPayload, headers);
                
                CorrelationData correlationData = new CorrelationData(correlationId);
                rabbitTemplate.send(DEFAULT_EXCHANGE, MAIN_ROUTING_KEY, amqpMessage, correlationData);
                
                int count = messageCounter.incrementAndGet();
                log.info("Mensaje enviado exitosamente a la cola principal. CorrelationId: {}, Contador: {}", 
                        correlationId, count);
                
                messageSink.emitNext(messageWithMeta, Sinks.EmitFailureHandler.FAIL_FAST);
                return messageWithMeta;
                
            } catch (JsonProcessingException e) {
                log.error("Error al serializar el mensaje: {}", e.getMessage());
                throw new RuntimeException("Error de serialización", e);
            }
        }).doOnError(error -> {
            log.error("Error enviando mensaje: {}", error.getMessage());
            handleSendError(message, error);
        }).retryWhen(reactor.util.retry.Retry.backoff(3, Duration.ofMillis(100))
                .doBeforeRetry(retrySignal -> {
                    log.warn("Reintentando envío del mensaje. Intento: {}", retrySignal.totalRetries() + 1);
                }));
    }

    private org.springframework.amqp.core.Message createAmqpMessage(String payload, Map<String, Object> headers) {
        org.springframework.amqp.core.MessageProperties props = new org.springframework.amqp.core.MessageProperties();
        props.setContentType(org.springframework.amqp.core.MessageProperties.CONTENT_TYPE_JSON);
        props.setDeliveryMode(org.springframework.amqp.core.MessageDeliveryMode.PERSISTENT);
        props.setPriority(1);
        
        headers.forEach(props::setHeader);
        
        return new org.springframework.amqp.core.Message(payload.getBytes(), props);
    }

    private void handleSendError(MessageModel message, Throwable error) {
        log.error("Error permanente al enviar mensaje después de reintentos: {}", error.getMessage());
        if (dlqEnabled) {
            sendToDlq(message, error);
        }
    }

    private void sendToDlq(MessageModel message, Throwable error) {
        try {
            MessageModel dlqMessage = message.withStatus(MessageStatus.FAILED);
            String jsonPayload = objectMapper.writeValueAsString(dlqMessage);
            
            Map<String, Object> dlqHeaders = new HashMap<>();
            dlqHeaders.put("original-exchange", DEFAULT_EXCHANGE);
            dlqHeaders.put("original-routing-key", MAIN_ROUTING_KEY);
            dlqHeaders.put("error-message", error.getMessage());
            dlqHeaders.put("error-type", error.getClass().getName());
            dlqHeaders.put("dlq-timestamp", System.currentTimeMillis());
            
            org.springframework.amqp.core.Message dlqAmqpMessage = createAmqpMessage(jsonPayload, dlqHeaders);
            rabbitTemplate.send(DEFAULT_EXCHANGE, DLQ_ROUTING_KEY, dlqAmqpMessage);
            
            log.info("Mensaje enviado a DLQ después de fallos. CorrelationId en headers.");
            
        } catch (JsonProcessingException e) {
            log.error("Error crítico: no se pudo enviar el mensaje a la DLQ: {}", e.getMessage());
        }
    }

    public Mono<Boolean> sendBatch(java.util.List<MessageModel> messages) {
        return Mono.fromCallable(() -> {
            messages.forEach(msg -> sendMessage(msg).subscribe(
                    success -> log.debug("Mensaje del batch enviado: {}", success),
                    error -> log.error("Error en batch: {}", error.getMessage())
            ));
            return true;
        });
    }

    public Mono<Long> getMessageCount() {
        return Mono.fromCallable(() -> (long) messageCounter.get());
    }

    public Sinks.Many<MessageModel> getMessageSink() {
        return messageSink;
    }

    @Bean
    public DirectExchange exchange() {
        return ExchangeBuilder.directExchange(DEFAULT_EXCHANGE)
                .durable(true)
                .withArgument("alternate-exchange", "protocolos.ae")
                .build();
    }

    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable(MAIN_QUEUE)
                .withArgument("x-dead-letter-exchange", DEFAULT_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .withArgument("x-message-ttl", 86400000)
                .build();
    }

    @Bean
    public Queue dlqQueue() {
        return QueueBuilder.durable(DLQ_QUEUE)
                .withArgument("x-message-ttl", 604800000)
                .build();
    }

    @Bean
    public Binding mainBinding(Queue mainQueue, DirectExchange exchange) {
        return BindingBuilder.bind(mainQueue).to(exchange).with(MAIN_ROUTING_KEY);
    }

    @Bean
    public Binding dlqBinding(Queue dlqQueue, DirectExchange exchange) {
        return BindingBuilder.bind(dlqQueue).to(exchange).with(DLQ_ROUTING_KEY);
    }
}