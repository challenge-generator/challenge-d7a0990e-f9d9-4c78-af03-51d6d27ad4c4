package com.integracion.protocolos.config;

import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketServer;
import io.rsocket.frame.decoder.FrameDecoder;
import io.rsocket.transport.ServerTransport;
import io.rsocket.transport.local.LocalServerTransport;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.transport.netty.server.NettyServerTransport;
import io.rsocket.util.DefaultPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.rsocket.context.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.messaging.rsocket.RSocketMessageHandler;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandlerConfigurer;
import org.springframework.messaging.rsocket.connectors.SimpleRSocketConnectorPool;
import org.springframework.util.unit.DataSize;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.resources.LoopResources;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Configuration(proxyBeanMethods = false)
public class RSocketConfig {

    private static final Logger log = LoggerFactory.getLogger(RSocketConfig.class);
    private static final int DEFAULT_PORT = 7000;
    private static final int KEEP_ALIVE_INTERVAL_SECONDS = 60;
    private static final int KEEP_ALIVE_MAX_LIFETIME_SECONDS = 120;
    private static final int MAX_PAYLOAD_SIZE_MB = 4;
    private static final int MAX_CONCURRENT_STREAMS = 100;

    private final Map<String, RSocket> connectedClients = new ConcurrentHashMap<>();

    @Bean
    public RSocketServerCustomizer rSocketServerCustomizer() {
        return server -> server
            .acceptor(setupAcceptor())
            .interceptors(interceptorRegistry -> {
                interceptorRegistry.forSocketTransport(transport -> {
                    log.info("Configuring RSocket transport: {}", transport.getClass().getSimpleName());
                });
            });
    }

    @Bean
    public SocketAcceptor setupAcceptor() {
        return (setup, sendingSocket) -> {
            log.info("RSocket connection established from: {}", setup.getAuthority());
            String clientId = setup.getAuthority();
            connectedClients.put(clientId, sendingSocket);

            return RSocket.of(
                requestResponse -> {
                    String route = requestResponse.getMetadataUtf8();
                    log.debug("RSocket request-response: {}", route);
                    return handleRequestResponse(requestResponse);
                },
                requestStream -> {
                    String route = requestStream.getMetadataUtf8();
                    log.debug("RSocket request-stream: {}", route);
                    return handleRequestStream(requestStream);
                },
                fireAndForget -> {
                    String route = fireAndForget.getMetadataUtf8();
                    log.debug("RSocket fire-and-forget: {}", route);
                    return handleFireAndForget(fireAndForget);
                },
                channel -> {
                    String route = channel.getMetadataUtf8();
                    log.debug("RSocket channel: {}", route);
                    return handleChannel(channel);
                },
                metadataPush -> {
                    log.debug("RSocket metadata-push received");
                    return handleMetadataPush(metadataPush);
                }
            );
        };
    }

    private Mono<org.reactivestreams.Publisher<io.rsocket.Payload>> handleRequestResponse(
            io.rsocket.Payload payload) {
        String data = payload.getDataUtf8();
        log.info("Processing request-response with data: {}", data);
        String responseData = "{\"response\":\"processed\",\"original\":\"" + data + "\"}";
        return Mono.just(DefaultPayload.create(responseData));
    }

    private Flux<io.rsocket.Payload> handleRequestStream(io.rsocket.Payload payload) {
        String data = payload.getDataUtf8();
        log.info("Processing request-stream with data: {}", data);
        return Flux.interval(Duration.ofMillis(100))
            .take(10)
            .map(i -> DefaultPayload.create(
                "{\"sequence\":" + i + ",\"data\":\"" + data + "\"}"
            ));
    }

    private Mono<Void> handleFireAndForget(io.rsocket.Payload payload) {
        String data = payload.getDataUtf8();
        log.info("Processing fire-and-forget: {}", data);
        return Mono.empty();
    }

    private Flux<io.rsocket.Payload> handleChannel(io.rsocket.Payload payload) {
        log.info("Processing channel communication");
        return Flux.empty();
    }

    private Mono<Void> handleMetadataPush(io.rsocket.Payload payload) {
        String metadata = payload.getMetadataUtf8();
        log.info("Received metadata-push: {}", metadata);
        return Mono.empty();
    }

    @Bean
    public RSocketMessageHandler rSocketMessageHandler(RSocketStrategies rSocketStrategies) {
        RSocketMessageHandler handler = new RSocketMessageHandler();
        handler.setRSocketStrategies(rSocketStrategies);
        handler.setAdapterRegistry(new ReactiveAdapterRegistry());
        return handler;
    }

    @Bean
    public RSocketStrategies rSocketStrategies() {
        return RSocketStrategies.builder()
            .encoder(new Jackson2CborEncoder())
            .decoder(new Jackson2CborDecoder())
            .dataBufferFactory((bufferFactory) -> bufferFactory)
            .maxInMemorySize(DataSize.ofMegabytes(MAX_PAYLOAD_SIZE_MB))
            .build();
    }

    @Bean
    public RSocketMessageHandlerConfigurer rSocketMessageHandlerConfigurer(
            RSocketMessageHandler handler) {
        return configurer -> {
            configurer.setMessageHandler(handler);
        };
    }

    @Bean
    public SimpleRSocketConnectorPool rSocketConnectorPool() {
        return SimpleRSocketConnectorPool.builder()
            .clientTransportFactory(uri -> TcpClientTransport.create(uri.getHost(), uri.getPort()))
            .maxConcurrentClients(10)
            .build();
    }

    @Bean
    public NettyServerTransport rSocketTransport(LoopResources loopResources) {
        return NettyServerTransport.create(loopResources, DEFAULT_PORT);
    }

    @Bean
    public LoopResources rsocketLoopResources() {
        return LoopResources.create("rsocket-server", 1, 4, true);
    }

    @Bean
    public FrameDecoder rsocketFrameDecoder() {
        return FrameDecoder.defaults();
    }

    public Map<String, RSocket> getConnectedClients() {
        return Map.copyOf(connectedClients);
    }

    public void disconnectClient(String clientId) {
        RSocket removed = connectedClients.remove(clientId);
        if (removed != null) {
            removed.dispose();
            log.info("Client {} disconnected and resources released", clientId);
        }
    }
}