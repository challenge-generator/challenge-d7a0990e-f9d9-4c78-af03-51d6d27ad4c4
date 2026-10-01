package com.integracion.protocolos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Hooks;

@SpringBootApplication
@EnableWebFlux
public class ProtocolosApplication {

    public static void main(String[] args) {
        Hooks.onOperatorDebug();
        SpringApplication.run(ProtocolosApplication.class, args);
    }

    @Bean
    public RSocketMessageHandler rSocketMessageHandler(RSocketStrategies rSocketStrategies) {
        RSocketMessageHandler handler = new RSocketMessageHandler();
        handler.setRSocketStrategies(rSocketStrategies);
        return handler;
    }

    @Bean
    public RSocketStrategies rSocketStrategies() {
        return RSocketStrategies.builder()
                .build();
    }
}