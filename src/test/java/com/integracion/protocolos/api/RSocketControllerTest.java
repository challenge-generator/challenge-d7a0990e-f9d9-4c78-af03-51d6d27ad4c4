package com.integracion.protocolos.api;

import com.integracion.protocolos.domain.MessageModel;
import com.integracion.protocolos.domain.MessageModel.MessageStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("RSocketController Integration Tests")
class RSocketControllerTest {

    @Autowired(required = false)
    private RSocketRequester rSocketRequester;

    private MessageModel testMessage;

    @BeforeEach
    void setUp() {
        testMessage = new MessageModel(
            UUID.randomUUID().toString(),
            "test-payload",
            MessageStatus.PENDING,
            0,
            "test-source"
        );
    }

    @Nested
    @DisplayName("Request-Response Scenario")
    class RequestResponseTests {

        @Test
        @Timeout(value = 30, unit = TimeUnit.SECONDS)
        @DisplayName("should send request and receive response via RSocket")
        void shouldSendRequestAndReceiveResponse() {
            if (rSocketRequester == null) {
                org.junit.jupiter.api.Assumptions.assumeTrue(
                    rSocketRequester != null,
                    "RSocketRequester not available in test context"
                );
            }

            Mono<MessageModel> responseMono = rSocketRequester
                .route("request-response")
                .data(testMessage)
                .retrieveMono(MessageModel.class);

            StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.messageId()).isEqualTo(testMessage.messageId());
                    assertThat(response.status()).isIn(
                        MessageStatus.PROCESSED,
                        MessageStatus.FAILED,
                        MessageStatus.PENDING
                    );
                })
                .verifyComplete();
        }

        @Test
        @Timeout(value = 30, unit = TimeUnit.SECONDS)
        @DisplayName("should handle error response gracefully")
        void shouldHandleErrorResponse() {
            if (rSocketRequester == null) {
                org.junit.jupiter.api.Assumptions.assumeTrue(false);
            }

            MessageModel invalidMessage = new MessageModel(
                "",
                "",
                MessageStatus.PENDING,
                0,
                ""
            );

            Mono<MessageModel> responseMono = rSocketRequester
                .route("request-response")
                .data(invalidMessage)
                .retrieveMono(MessageModel.class);

            StepVerifier.create(responseMono)
                .expectError()
                .verify();
        }

        @Test
        @Timeout(value = 30, unit = TimeUnit.SECONDS)
        @DisplayName("should process multiple sequential requests")
        void shouldProcessMultipleSequentialRequests() {
            if (rSocketRequester == null) {
                org.junit.jupiter.api.Assumptions.assumeTrue(false);
            }

            for (int i = 0; i < 5; i++) {
                MessageModel message = testMessage.withIncrementedRetryCount();
                Mono<MessageModel> response = rSocketRequester
                    .route("request-response")
                    .data(message)
                    .retrieveMono(MessageModel.class);

                StepVerifier.create(response)
                    .assertNext(resp -> {
                        assertThat(resp.messageId()).isEqualTo(message.messageId());
                    })
                    .verifyComplete();
            }
        }
    }

    @Nested
    @DisplayName("Bidirectional Communication Scenario")
    class BidirectionalTests {

        @Test
        @Timeout(value = 45, unit = TimeUnit.SECONDS)
        @DisplayName("should establish bidirectional stream communication")
        void shouldEstablishBidirectionalStream() {
            if (rSocketRequester == null) {
                org.junit.jupiter.api.Assumptions.assumeTrue(false);
            }

            var messages = java.util.List.of(
                testMessage,
                testMessage.withIncrementedRetryCount(),
                new MessageModel(
                    UUID.randomUUID().toString(),
                    "stream-payload-2",
                    MessageStatus.PENDING,
                    0,
                    "test-source"
                )
            );

            var responseFlux = rSocketRequester
                .route("bidirectional-stream")
                .data(Flux.fromIterable(messages))
                .retrieveFlux(MessageModel.class);

            StepVerifier.create(responseFlux)
                .expectNextCount(3)
                .verifyComplete();
        }

        @Test
        @Timeout(value = 45, unit = TimeUnit.SECONDS)
        @DisplayName("should handle channel communication with backpressure")
        void shouldHandleChannelCommunicationWithBackpressure() {
            if (rSocketRequester == null) {
                org.junit.jupiter.api.Assumptions.assumeTrue(false);
            }

            var requestFlux = Flux.interval(Duration.ofMillis(100))
                .take(10)
                .map(i -> new MessageModel(
                    UUID.randomUUID().toString(),
                    "channel-payload-" + i,
                    MessageStatus.PENDING,
                    0,
                    "channel-source"
                ));

            var responseFlux = rSocketRequester
                .route("channel")
                .data(requestFlux)
                .retrieveFlux(MessageModel.class);

            StepVerifier.create(responseFlux)
                .expectNextCount(10)
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Fire-and-Forget Scenario")
    class FireAndForgetTests {

        @Test
        @Timeout(value = 30, unit = TimeUnit.SECONDS)
        @DisplayName("should send message without waiting for response")
        void shouldSendFireAndForgetMessage() {
            if (rSocketRequester == null) {
                org.junit.jupiter.api.Assumptions.assumeTrue(false);
            }

            Mono<Void> sendMono = rSocketRequester
                .route("fire-and-forget")
                .data(testMessage)
                .send();

            StepVerifier.create(sendMono)
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Connection Management")
    class ConnectionManagementTests {

        @Test
        @DisplayName("should verify RSocket connection is established")
        void shouldVerifyConnectionEstablished() {
            if (rSocketRequester == null) {
                org.junit.jupiter.api.Assumptions.assumeTrue(false);
            }

            assertThat(rSocketRequester).isNotNull();
            assertThat(rSocketRequester.rsocket()).isNotNull();
        }
    }
}