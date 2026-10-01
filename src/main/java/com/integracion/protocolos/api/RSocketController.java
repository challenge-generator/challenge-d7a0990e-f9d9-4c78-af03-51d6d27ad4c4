package com.integracion.protocolos.api;

import com.integracion.protocolos.domain.MessageModel;
import com.integracion.protocolos.domain.MessageModel.MessageStatus;
import com.integracion.protocolos.domain.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.messaging.rsocket.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class RSocketController {

    private static final Logger log = LoggerFactory.getLogger(RSocketController.class);
    private final MessageService messageService;
    private final Map<String, RSocketRequester> connectedClients = new ConcurrentHashMap<>();

    public RSocketController(MessageService messageService) {
        this.messageService = messageService;
    }

    @ConnectMapping("connect")
    public void onConnect(RSocketRequester requester) {
        String clientId = UUID.randomUUID().toString();
        connectedClients.put(clientId, requester);
        log.info("Cliente RSocket conectado: {}", clientId);
        requester.rsocket().onClose().doFinally(signal -> {
            connectedClients.remove(clientId);
            log.info("Cliente RSocket desconectado: {}", clientId);
        }).subscribe();
    }

    @MessageMapping("request-response")
    public Mono<MessageModel> requestResponse(MessageModel message) {
        log.info("RSocket request-response recibido: id={}, payload={}", 
                 message.messageId(), message.payload());
        return messageService.processMessage(message)
                .doOnNext(result -> log.info("RSocket request-response completado: id={}", 
                        result.messageId()));
    }

    @MessageMapping("fire-and-forget")
    public Mono<Void> fireAndForget(MessageModel message) {
        log.info("RSocket fire-and-forget recibido: id={}, payload={}", 
                 message.messageId(), message.payload());
        return messageService.processMessage(message)
                .doOnNext(result -> log.info("RSocket fire-and-forget procesado: id={}", 
                        result.messageId()))
                .then();
    }

    @MessageMapping("request-stream")
    public Flux<MessageModel> requestStream(MessageModel initialMessage) {
        log.info("RSocket request-stream iniciado: id={}", initialMessage.messageId());
        return messageService.processMessageStream(initialMessage)
                .delayElements(Duration.ofMillis(100))
                .doOnNext(msg -> log.info("RSocket request-stream emitiendo: id={}", msg.messageId()));
    }

    @MessageMapping("channel")
    public Flux<MessageModel> channel(Flux<MessageModel> messages) {
        log.info("RSocket channel iniciado");
        return messages
                .windowTime(Duration.ofSeconds(5))
                .flatMap(window -> 
                    window.collectList()
                            .filter(list -> !list.isEmpty())
                            .flatMapMany(list -> {
                                MessageModel aggregated = aggregateMessages(list);
                                return messageService.processMessage(aggregated);
                            })
                )
                .doOnComplete(() -> log.info("RSocket channel completado"));
    }

    private MessageModel aggregateMessages(java.util.List<MessageModel> messages) {
        String combinedPayload = messages.stream()
                .map(MessageModel::payload)
                .reduce((a, b) -> a + ";" + b)
                .orElse("");
        return new MessageModel(
                UUID.randomUUID().toString(),
                combinedPayload,
                MessageStatus.PROCESSING,
                Instant.now().toString(),
                "aggregated",
                0
        );
    }

    public Mono<Void> broadcastToAll(MessageModel message) {
        return Flux.fromIterable(connectedClients.values())
                .flatMap(requester -> requester.route("broadcast").data(message))
                .then();
    }

    public int getConnectedClientsCount() {
        return connectedClients.size();
    }
}