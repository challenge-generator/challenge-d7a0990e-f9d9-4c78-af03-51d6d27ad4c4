package com.integracion.protocolos.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Modelo canónico para mensajes intercambiados entre protocolos (RSocket, gRPC, AMQP).
 * Representa la estructura base que todos los protocolos deben ser capaces de manejar.
 */
public record MessageModel(
    UUID messageId,
    String correlationId,
    String sourceService,
    String targetService,
    String payloadType,
    String payload,
    Map<String, String> metadata,
    Instant timestamp,
    MessageStatus status,
    int retryCount
) {
    /**
     * Estados posibles de un mensaje en el flujo de procesamiento.
     */
    public enum MessageStatus {
        RECEIVED,
        PROCESSING,
        COMPLETED,
        FAILED,
        RETRYING,
        DLQ
    }

    /**
     * Constructor que inicializa un mensaje con valores por defecto.
     * @param correlationId Identificador de correlación para trazabilidad.
     * @param sourceService Servicio origen del mensaje.
     * @param targetService Servicio destino del mensaje.
     * @param payloadType Tipo de payload (ej: "Order", "Payment").
     * @param payload Contenido del mensaje en formato serializado.
     * @param metadata Metadatos adicionales para enrutamiento y manejo.
     */
    public MessageModel {
        if (messageId == null) {
            messageId = UUID.randomUUID();
        }
        if (correlationId == null || correlationId.isBlank()) {
            throw new IllegalArgumentException("correlationId no puede ser nulo o vacío");
        }
        if (sourceService == null || sourceService.isBlank()) {
            throw new IllegalArgumentException("sourceService no puede ser nulo o vacío");
        }
        if (targetService == null || targetService.isBlank()) {
            throw new IllegalArgumentException("targetService no puede ser nulo o vacío");
        }
        if (payloadType == null || payloadType.isBlank()) {
            throw new IllegalArgumentException("payloadType no puede ser nulo o vacío");
        }
        if (payload == null) {
            payload = "";
        }
        if (metadata == null) {
            metadata = Map.of();
        }
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        if (status == null) {
            status = MessageStatus.RECEIVED;
        }
    }

    /**
     * Crea una nueva instancia del mensaje con estado actualizado.
     * @param newStatus Nuevo estado del mensaje.
     * @return Nueva instancia con el estado actualizado.
     */
    public MessageModel withStatus(MessageStatus newStatus) {
        return new MessageModel(
            this.messageId,
            this.correlationId,
            this.sourceService,
            this.targetService,
            this.payloadType,
            this.payload,
            this.metadata,
            this.timestamp,
            newStatus,
            this.retryCount
        );
    }

    /**
     * Crea una nueva instancia del mensaje con conteo de reintentos incrementado.
     * @return Nueva instancia con retryCount incrementado.
     */
    public MessageModel withIncrementedRetryCount() {
        return new MessageModel(
            this.messageId,
            this.correlationId,
            this.sourceService,
            this.targetService,
            this.payloadType,
            this.payload,
            this.metadata,
            this.timestamp,
            this.status,
            this.retryCount + 1
        );
    }

    /**
     * Verifica si el mensaje ha excedido el límite máximo de reintentos.
     * @param maxRetries Límite máximo de reintentos permitido.
     * @return true si el mensaje debe ser movido a DLQ.
     */
    public boolean isRetryLimitExceeded(int maxRetries) {
        return this.retryCount >= maxRetries;
    }
}