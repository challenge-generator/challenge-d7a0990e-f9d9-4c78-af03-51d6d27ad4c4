package com.integracion.protocolos.domain;


import com.integracion.protocolos.config.MessageStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * Servicio de dominio que orquesta la lógica de negocio para procesamiento de mensajes.
 * Maneja la transformación, enrutamiento y política de reintentos para mensajes canónicos.
 */
@Service
public class MessageService {
    private static final int MAX_RETRIES = 3;
    private static final Duration RETRY_DELAY = Duration.ofMillis(500);

    /**
     * Procesa un mensaje canónico aplicando transformación y enrutamiento.
     * @param message Mensaje canónico a procesar.
     * @param transformer Función que transforma el payload del mensaje.
     * @param router Función que determina el destino del mensaje basado en su metadata.
     * @return Mono con el mensaje procesado.
     */
    public Mono<MessageModel> processMessage(
            MessageModel message,
            Function<String, String> transformer,
            Function<Map<String, String>, String> router) {
        return Mono.just(message)
                .flatMap(msg -> transformPayload(msg, transformer))
                .flatMap(msg -> routeMessage(msg, router))
                .onErrorResume(e -> handleProcessingError(message, e))
                .retryWhen(Retry.backoff(MAX_RETRIES, RETRY_DELAY)
                        .filter(this::isRetryableError)
                        .doBeforeRetry(retrySignal -> {
                            System.out.println("Reintento #" + retrySignal.failure().getMessage());
                        }))
                .onErrorResume(e -> moveToDLQ(message, e));
    }

    /**
     * Transforma el payload de un mensaje usando la función proporcionada.
     * @param message Mensaje a transformar.
     * @param transformer Función de transformación.
     * @return Mono con el mensaje transformado.
     */
    private Mono<MessageModel> transformPayload(
            MessageModel message,
            Function<String, String> transformer) {
        return Mono.fromCallable(() -> {
            String transformedPayload = transformer.apply(message.payload());
            return message.withStatus(MessageModel.MessageStatus.PROCESSING)
                    .withPayload(transformedPayload);
        }).onErrorMap(e -> new MessageProcessingException(
                "Error transformando payload del mensaje " + message.messageId(), e));
    }

    /**
     * Determina el destino del mensaje basado en su metadata.
     * @param message Mensaje a enrutar.
     * @param router Función de enrutamiento.
     * @return Mono con el mensaje enrutado.
     */
    private Mono<MessageModel> routeMessage(
            MessageModel message,
            Function<Map<String, String>, String> router) {
        return Mono.fromCallable(() -> {
            String newTarget = router.apply(message.metadata());
            return message.withStatus(MessageModel.MessageStatus.COMPLETED)
                    .withTargetService(newTarget);
        }).onErrorMap(e -> new MessageProcessingException(
                "Error enrutando mensaje " + message.messageId(), e));
    }

    /**
     * Maneja errores recuperables durante el procesamiento.
     * @param message Mensaje que falló.
     * @param error Excepción ocurrida.
     * @return Mono con el mensaje marcado para reintento.
     */
    private Mono<MessageModel> handleProcessingError(MessageModel message, Throwable error) {
        return Mono.just(message.withStatus(MessageModel.MessageStatus.RETRYING)
                .withIncrementedRetryCount())
                .doOnNext(msg -> System.err.println(
                        "Error procesando mensaje " + msg.messageId() + ": " + error.getMessage()));
    }

    /**
     * Mueve un mensaje a la cola de mensajes muertos (DLQ) cuando falla permanentemente.
     * @param message Mensaje fallido.
     * @param error Excepción ocurrida.
     * @return Mono con el mensaje marcado como DLQ.
     */
    private Mono<MessageModel> moveToDLQ(MessageModel message, Throwable error) {
        return Mono.just(message.withStatus(MessageModel.MessageStatus.DLQ))
                .doOnNext(msg -> System.err.println(
                        "Mensaje " + msg.messageId() + " movido a DLQ: " + error.getMessage()));
    }

    /**
     * Verifica si un error es recuperable para reintento.
     * @param error Excepción ocurrida.
     * @return true si el error es recuperable.
     */
    private boolean isRetryableError(Throwable error) {
        return !(error instanceof NonRetryableException);
    }

    /**
     * Actualiza el servicio destino de un mensaje.
     * @param targetService Nuevo servicio destino.
     * @return Nueva instancia del mensaje con el servicio destino actualizado.
     */
    private MessageModel withTargetService(String targetService) {
        return new MessageModel(
                this.messageId(),
                this.correlationId(),
                this.sourceService(),
                targetService,
                this.payloadType(),
                this.payload(),
                this.metadata(),
                this.timestamp(),
                this.status(),
                this.retryCount()
        );
    }

    /**
     * Procesa un flujo de mensajes con política de reintentos por mensaje.
     * @param messages Flujo de mensajes a procesar.
     * @param transformer Función de transformación.
     * @param router Función de enrutamiento.
     * @return Flux de mensajes procesados.
     */
    public Flux<MessageModel> processMessageStream(
            Flux<MessageModel> messages,
            Function<String, String> transformer,
            Function<Map<String, String>, String> router) {
        return messages.flatMap(message -> processMessage(message, transformer, router));
    }

    /**
     * Excepción para errores no recuperables durante el procesamiento.
     */
    public static class NonRetryableException extends RuntimeException {
        public NonRetryableException(String message) {
            super(message);
        }

        public NonRetryableException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Excepción para errores durante el procesamiento de mensajes.
     */
    public static class MessageProcessingException extends RuntimeException {
        public MessageProcessingException(String message) {
            super(message);
        }

        public MessageProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}