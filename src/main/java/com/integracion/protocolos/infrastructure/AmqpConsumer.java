package com.integracion.protocolos.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integracion.protocolos.domain.MessageModel;
import com.integracion.protocolos.domain.MessageModel.MessageStatus;
import com.integracion.protocolos.domain.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class AmqpConsumer {

    private static final Logger log = LoggerFactory.getLogger(AmqpConsumer.class);
    private static final String MAIN_QUEUE = "protocolos.main.queue";
    private static final String DLQ_QUEUE = "protocolos.dlq.queue";
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_INITIAL_INTERVAL = 1000L;
    private static final double RETRY_MULTIPLIER = 2.0;
    private static final long RETRY_MAX_INTERVAL = 10000L;

    private final MessageService messageService;
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;
    private final AtomicInteger processedCounter;
    private final AtomicInteger dlqCounter;

    @Value("${amqp.consumer.prefetch:10}")
    private int prefetchCount;

    @Value("${amqp.consumer.concurrent:5}")
    private int concurrentConsumers;

    @Value("${amqp.consumer.retry.enabled:true}")
    private boolean retryEnabled;

    public AmqpConsumer(MessageService messageService, ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        this.messageService = messageService;
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.processedCounter = new AtomicInteger(0);
        this.dlqCounter = new AtomicInteger(0);
    }

    @RabbitListener(queues = MAIN_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public Mono<Void> consumeMessage(MessageModel message) {
        String messageId = extractMessageId(message);
        int currentRetry = extractRetryCount(message);
        
        log.info("Mensaje recibido. ID: {}, Retry: {}, Status: {}", 
                messageId, currentRetry, message.status());

        return messageService.processMessage(message)
                .doOnSuccess(result -> {
                    int count = processedCounter.incrementAndGet();
                    log.info("Mensaje procesado exitosamente. ID: {}, Total procesados: {}", 
                            messageId, count);
                })
                .doOnError(error -> {
                    log.error("Error procesando mensaje ID: {}. Error: {}", messageId, error.getMessage());
                    handleProcessingError(message, error, currentRetry);
                })
                .then();
    }

    private void handleProcessingError(MessageModel message, Throwable error, int currentRetry) {
        if (retryEnabled && currentRetry < MAX_RETRIES) {
            scheduleRetry(message, currentRetry + 1);
        } else {
            moveToDlq(message, error);
        }
    }

    private void scheduleRetry(MessageModel message, int nextRetry) {
        MessageModel retryMessage = message
                .withStatus(MessageStatus.RETRY)
                .withIncrementedRetryCount();
        
        long delay = calculateBackoffDelay(nextRetry);
        
        log.info("Programando reintento {} para mensaje. Delay: {}ms", nextRetry, delay);
        
        Mono.delay(Duration.ofMillis(delay))
                .flatMap(tick -> {
                    try {
                        String jsonPayload = objectMapper.writeValueAsString(retryMessage);
                        Map<String, Object> headers = new HashMap<>();
                        headers.put("retryCount", nextRetry);
                        headers.put("retryScheduled", true);
                        
                        org.springframework.amqp.core.MessageProperties props = 
                                new org.springframework.amqp.core.MessageProperties();
                        props.setContentType(org.springframework.amqp.core.MessageProperties.CONTENT_TYPE_JSON);
                        props.setDeliveryMode(org.springframework.amqp.core.MessageDeliveryMode.PERSISTENT);
                        headers.forEach(props::setHeader);
                        
                        org.springframework.amqp.core.Message amqpMessage = 
                                new org.springframework.amqp.core.Message(jsonPayload.getBytes(), props);
                        rabbitTemplate.send("protocolos.exchange", "protocolos.message", amqpMessage);
                        
                        return Mono.just(true);
                    } catch (Exception e) {
                        log.error("Error al reprogramar mensaje: {}", e.getMessage());
                        return Mono.error(e);
                    }
                })
                .subscribe(
                        success -> log.debug("Mensaje reprogramado exitosamente"),
                        error -> log.error("Error en reprogramación: {}", error.getMessage())
                );
    }

    private long calculateBackoffDelay(int retryAttempt) {
        long delay = (long) (RETRY_INITIAL_INTERVAL * Math.pow(RETRY_MULTIPLIER, retryAttempt - 1));
        return Math.min(delay, RETRY_MAX_INTERVAL);
    }

    private void moveToDlq(MessageModel message, Throwable error) {
        try {
            MessageModel failedMessage = message.withStatus(MessageStatus.FAILED);
            String jsonPayload = objectMapper.writeValueAsString(failedMessage);
            
            Map<String, Object> dlqHeaders = new HashMap<>();
            dlqHeaders.put("original-queue", MAIN_QUEUE);
            dlqHeaders.put("error-message", error.getMessage());
            dlqHeaders.put("error-type", error.getClass().getName());
            dlqHeaders.put("final-retry-count", extractRetryCount(message));
            dlqHeaders.put("moved-to-dlq-timestamp", System.currentTimeMillis());
            
            org.springframework.amqp.core.MessageProperties props = 
                    new org.springframework.amqp.core.MessageProperties();
            props.setContentType(org.springframework.amqp.core.MessageProperties.CONTENT_TYPE_JSON);
            props.setDeliveryMode(org.springframework.amqp.core.MessageDeliveryMode.PERSISTENT);
            dlqHeaders.forEach(props::setHeader);
            
            org.springframework.amqp.core.Message dlqMessage = 
                    new org.springframework.amqp.core.Message(jsonPayload.getBytes(), props);
            
            rabbitTemplate.send("protocolos.exchange", "protocolos.dlq", dlqMessage);
            
            int count = dlqCounter.incrementAndGet();
            log.warn("Mensaje movido a DLQ. Total DLQ: {}. Error: {}", count, error.getMessage());
            
        } catch (Exception e) {
            log.error("Error crítico al mover mensaje a DLQ: {}", e.getMessage());
        }
    }

    @RabbitListener(queues = DLQ_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public Mono<Void> consumeDlqMessage(MessageModel message) {
        log.warn("Mensaje recibido de DLQ. ID: {}, Status: {}, Payload: {}", 
                extractMessageId(message), message.status(), message.payload());
        
        return Mono.empty();
    }

    private String extractMessageId(MessageModel message) {
        return message.correlationId() != null ? message.correlationId() : "unknown";
    }

    private int extractRetryCount(MessageModel message) {
        return message.retryCount() != null ? message.retryCount() : 0;
    }

    public Mono<Long> getProcessedCount() {
        return Mono.just((long) processedCounter.get());
    }

    public Mono<Long> getDlqCount() {
        return Mono.just((long) dlqCounter.get());
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(concurrentConsumers);
        factory.setMaxConcurrentConsumers(concurrentConsumers * 2);
        factory.setPrefetchCount(prefetchCount);
        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(jsonMessageConverter());
        
        factory.setAdviceChain(org.springframework.retry.interceptor.RetryInterceptorBuilder
                .stateless()
                .retryPolicy(new SimpleRetryPolicy(3))
                .backOffPolicy(createBackOffPolicy())
                .build());
        
        return factory;
    }

    private Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setTypeIdPropertyName("__typeId__");
        return converter;
    }

    private ExponentialBackOffPolicy createBackOffPolicy() {
        ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
        policy.setInitialInterval(RETRY_INITIAL_INTERVAL);
        policy.setMultiplier(RETRY_MULTIPLIER);
        policy.setMaxInterval(RETRY_MAX_INTERVAL);
        return policy;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(MAX_RETRIES);
        retryTemplate.setRetryPolicy(retryPolicy);
        
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(RETRY_INITIAL_INTERVAL);
        backOffPolicy.setMultiplier(RETRY_MULTIPLIER);
        backOffPolicy.setMaxInterval(RETRY_MAX_INTERVAL);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        
        return retryTemplate;
    }
}