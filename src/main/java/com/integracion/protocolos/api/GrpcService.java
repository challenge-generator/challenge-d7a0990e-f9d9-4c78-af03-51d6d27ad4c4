package com.integracion.protocolos.api;

import com.integracion.protocolos.domain.MessageModel;
import com.integracion.protocolos.domain.MessageModel.MessageStatus;
import com.integracion.protocolos.domain.MessageService;
import com.integracion.protocolos.proto.ProcessRequest;
import com.integracion.protocolos.proto.ProcessResponse;
import com.integracion.protocolos.proto.StreamRequest;
import com.integracion.protocolos.proto.StreamResponse;
import com.integracion.protocolos.proto.ServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@GrpcService
public class GrpcService extends ServiceGrpc.ServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(GrpcService.class);
    private final MessageService messageService;

    public GrpcService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void processMessage(ProcessRequest request, StreamObserver<ProcessResponse> responseObserver) {
        log.info("gRPC processMessage recibido: requestId={}, payload={}", 
                 request.getRequestId(), request.getPayload());

        MessageModel message = toMessageModel(request);

        messageService.processMessage(message)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        result -> {
                            ProcessResponse response = toProcessResponse(result);
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                            log.info("gRPC processMessage completado: requestId={}", 
                                    request.getRequestId());
                        },
                        error -> {
                            log.error("gRPC processMessage error: requestId={}, error={}", 
                                    request.getRequestId(), error.getMessage());
                            responseObserver.onError(error);
                        }
                );
    }

    @Override
    public void processMessageStream(StreamRequest request, StreamObserver<StreamResponse> responseObserver) {
        log.info("gRPC processMessageStream iniciado: requestId={}, count={}", 
                 request.getRequestId(), request.getMessageCount());

        MessageModel initialMessage = toMessageModel(request);
        AtomicInteger processedCount = new AtomicInteger(0);
        int targetCount = request.getMessageCount();

        messageService.processMessageStream(initialMessage)
                .takeWhile(msg -> processedCount.get() < targetCount)
                .subscribeOn(Schedulers.parallel())
                .subscribe(
                        result -> {
                            int current = processedCount.incrementAndGet();
                            StreamResponse response = toStreamResponse(result, current);
                            responseObserver.onNext(response);
                            log.debug("gRPC stream emitiendo mensaje {} de {}", 
                                    current, targetCount);
                            if (current >= targetCount) {
                                responseObserver.onCompleted();
                                log.info("gRPC processMessageStream completado: requestId={}, total={}", 
                                        request.getRequestId(), current);
                            }
                        },
                        error -> {
                            log.error("gRPC processMessageStream error: requestId={}, error={}", 
                                    request.getRequestId(), error.getMessage());
                            responseObserver.onError(error);
                        }
                );
    }

    @Override
    public StreamObserver<ProcessRequest> bidirectionalStream(StreamObserver<ProcessResponse> responseObserver) {
        log.info("gRPC bidirectionalStream iniciado");
        AtomicInteger requestCounter = new AtomicInteger(0);

        return new StreamObserver<ProcessRequest>() {
            @Override
            public void onNext(ProcessRequest request) {
                int requestNum = requestCounter.incrementAndGet();
                log.debug("gRPC bidirectionalStream recibiendo mensaje {}", requestNum);

                MessageModel message = toMessageModel(request);
                messageService.processMessage(message)
                        .subscribeOn(Schedulers.boundedElastic())
                        .subscribe(
                                result -> {
                                    ProcessResponse response = toProcessResponse(result);
                                    responseObserver.onNext(response);
                                    log.debug("gRPC bidirectionalStream enviando respuesta {}", 
                                            requestNum);
                                },
                                error -> {
                                    log.error("gRPC bidirectionalStream error en mensaje {}: {}", 
                                            requestNum, error.getMessage());
                                    responseObserver.onError(error);
                                }
                        );
            }

            @Override
            public void onError(Throwable t) {
                log.error("gRPC bidirectionalStream error: {}", t.getMessage());
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                log.info("gRPC bidirectionalStream completado: {} mensajes procesados", 
                        requestCounter.get());
                responseObserver.onCompleted();
            }
        };
    }

    private MessageModel toMessageModel(ProcessRequest request) {
        return new MessageModel(
                request.getRequestId().isEmpty() ? UUID.randomUUID().toString() : request.getRequestId(),
                request.getPayload(),
                MessageStatus.PENDING,
                Instant.now().toString(),
                "grpc",
                0
        );
    }

    private MessageModel toMessageModel(StreamRequest request) {
        return new MessageModel(
                request.getRequestId().isEmpty() ? UUID.randomUUID().toString() : request.getRequestId(),
                request.getPayload(),
                MessageStatus.PENDING,
                Instant.now().toString(),
                "grpc-stream",
                0
        );
    }

    private ProcessResponse toProcessResponse(MessageModel message) {
        return ProcessResponse.newBuilder()
                .setResponseId(message.messageId())
                .setPayload(message.payload())
                .setStatus(message.status().name())
                .setTimestamp(message.timestamp())
                .setSuccess(message.status() == MessageStatus.COMPLETED)
                .build();
    }

    private StreamResponse toStreamResponse(MessageModel message, int sequenceNumber) {
        return StreamResponse.newBuilder()
                .setResponseId(message.messageId())
                .setPayload(message.payload())
                .setStatus(message.status().name())
                .setTimestamp(message.timestamp())
                .setSequenceNumber(sequenceNumber)
                .setSuccess(message.status() == MessageStatus.COMPLETED)
                .build();
    }
}