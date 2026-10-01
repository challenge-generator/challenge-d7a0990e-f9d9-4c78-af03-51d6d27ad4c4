package com.integracion.protocolos.api;

import com.integracion.protocolos.domain.MessageModel;
import com.integracion.protocolos.domain.MessageModel.MessageStatus;
import com.integracion.protocolos.domain.MessageService;
import com.integracion.protocolos.infrastructure.AmqpProducer;
import com.integracion.protocolos.infrastructure.AmqpConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Controller
public class GraphQLController {

    private static final Logger log = LoggerFactory.getLogger(GraphQLController.class);
    
    private final MessageService messageService;
    private final AmqpProducer amqpProducer;
    private final AmqpConsumer amqpConsumer;
    private final Map<String, MessageModel> messageCache;

    public GraphQLController(MessageService messageService, 
                             AmqpProducer amqpProducer, 
                             AmqpConsumer amqpConsumer) {
        this.messageService = messageService;
        this.amqpProducer = amqpProducer;
        this.amqpConsumer = amqpConsumer;
        this.messageCache = new ConcurrentHashMap<>();
    }

    @QueryMapping
    public MessagePayload message(@Argument String id) {
        log.debug("Consultando mensaje con ID: {}", id);
        MessageModel cached = messageCache.get(id);
        if (cached != null) {
            return toPayload(cached);
        }
        return null;
    }

    @QueryMapping
    public List<MessagePayload> allMessages() {
        log.debug("Consultando todos los mensajes");
        return new ArrayList<>(messageCache.values()).stream()
                .map(this::toPayload)
                .collect(Collectors.toList());
    }

    @QueryMapping
    public MessageStats stats() {
        log.debug("Consultando estadísticas de mensajes");
        long total = messageCache.size();
        long sent = messageCache.values().stream()
                .filter(m -> m.status() == MessageStatus.SENT)
                .count();
        long processed = messageCache.values().stream()
                .filter(m -> m.status() == MessageStatus.PROCESSED)
                .count();
        long failed = messageCache.values().stream()
                .filter(m -> m.status() == MessageStatus.FAILED)
                .count();
        long retry = messageCache.values().stream()
                .filter(m -> m.status() == MessageStatus.RETRY)
                .count();
        
        return new MessageStats(total, sent, processed, failed, retry);
    }

    @QueryMapping
    public Mono<ProcessingResult> processMessage(@Argument MessageInput input) {
        log.info("Procesando mensaje vía GraphQL. Payload: {}", input.payload());
        
        MessageModel message = new MessageModel(
                UUID.randomUUID().toString(),
                input.payload(),
                MessageStatus.PENDING,
                Instant.now().toString(),
                input.targetService() != null ? input.targetService() : "default-service",
                0,
                null
        );
        
        return messageService.processMessage(message)
                .doOnSuccess(result -> {
                    messageCache.put(result.correlationId(), result);
                    log.info("Mensaje procesado exitosamente. ID: {}", result.correlationId());
                })
                .map(this::toProcessingResult)
                .onErrorResume(error -> {
                    log.error("Error procesando mensaje: {}", error.getMessage());
                    return Mono.just(new ProcessingResult(
                            false,
                            error.getMessage(),
                            null,
                            0
                    ));
                });
    }

    @MutationMapping
    public Mono<MessagePayload> sendMessage(@Argument MessageInput input) {
        log.info("Enviando mensaje vía GraphQL. Payload: {}", input.payload());
        
        MessageModel message = new MessageModel(
                UUID.randomUUID().toString(),
                input.payload(),
                MessageStatus.CREATED,
                Instant.now().toString(),
                input.targetService() != null ? input.targetService() : "default-service",
                0,
                null
        );
        
        return amqpProducer.sendMessage(message)
                .doOnSuccess(result -> {
                    messageCache.put(result.correlationId(), result);
                    log.info("Mensaje enviado exitosamente. ID: {}", result.correlationId());
                })
                .map(this::toPayload)
                .onErrorResume(error -> {
                    log.error("Error enviando mensaje: {}", error.getMessage());
                    MessageModel errorMessage = message.withStatus(MessageStatus.FAILED);
                    messageCache.put(errorMessage.correlationId(), errorMessage);
                    return Mono.just(toPayload(errorMessage));
                });
    }

    @MutationMapping
    public Mono<ProcessingResult> reprocessMessage(@Argument String id) {
        log.info("Reprocesando mensaje. ID: {}", id);
        
        MessageModel cached = messageCache.get(id);
        if (cached == null) {
            return Mono.just(new ProcessingResult(false, "Mensaje no encontrado", null, 0));
        }
        
        MessageModel retryMessage = cached.withStatus(MessageStatus.PENDING)
                .withIncrementedRetryCount();
        
        return messageService.processMessage(retryMessage)
                .doOnSuccess(result -> {
                    messageCache.put(result.correlationId(), result);
                    log.info("Mensaje reprocesado. ID: {}", result.correlationId());
                })
                .map(this::toProcessingResult)
                .onErrorResume(error -> {
                    log.error("Error reprocesando mensaje: {}", error.getMessage());
                    return Mono.just(new ProcessingResult(false, error.getMessage(), null, 0));
                });
    }

    @MutationMapping
    public Mono<Boolean> deleteMessage(@Argument String id) {
        log.info("Eliminando mensaje. ID: {}", id);
        MessageModel removed = messageCache.remove(id);
        return Mono.just(removed != null);
    }

    @QueryMapping
    public Mono<QueueStats> queueStats() {
        return Mono.zip(
                amqpProducer.getMessageCount(),
                amqpConsumer.getProcessedCount(),
                amqpConsumer.getDlqCount()
        ).map(tuple -> new QueueStats(
                tuple.getT1(),
                tuple.getT2(),
                tuple.getT3(),
                tuple.getT1() - tuple.getT2()
        ));
    }

    @SchemaMapping(typeName = "MessagePayload")
    public String status(MessagePayload payload) {
        return payload.status().name();
    }

    private MessagePayload toPayload(MessageModel model) {
        return new MessagePayload(
                model.correlationId(),
                model.payload(),
                model.status(),
                model.timestamp(),
                model.targetService(),
                model.retryCount()
        );
    }

    private ProcessingResult toProcessingResult(MessageModel model) {
        return new ProcessingResult(
                model.status() == MessageStatus.PROCESSED,
                "Mensaje procesado",
                model.correlationId(),
                model.retryCount()
        );
    }

    public record MessagePayload(
            String id,
            String payload,
            MessageStatus status,
            String timestamp,
            String targetService,
            Integer retryCount
    ) {}

    public record MessageInput(
            String payload,
            String targetService
    ) {}

    public record MessageStats(
            long total,
            long sent,
            long processed,
            long failed,
            long retry
    ) {}

    public record ProcessingResult(
            boolean success,
            String message,
            String messageId,
            int retryCount
    ) {}

    public record QueueStats(
            long queued,
            long processed,
            long dlq,
            long pending
    ) {}
}