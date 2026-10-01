package com.integracion.protocolos.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.grpc.ServiceDescriptor;
import io.grpc.StatusRuntimeException;
import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.health.v1.HealthGrpc;
import io.grpc.protobuf.services.HealthService;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.stub.MetadataUtils;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Configuration(proxyBeanMethods = false)
public class GrpcConfig implements InitializingBean, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(GrpcConfig.class);
    private static final int GRPC_PORT = 9090;
    private static final int MAX_MESSAGE_SIZE_MB = 10;
    private static final int MAX_INBOUND_MESSAGE_SIZE_MB = 15;
    private static final int MAX_OUTBOUND_MESSAGE_SIZE_MB = 15;
    private static final int KEEP_ALIVE_TIME_SECONDS = 60;
    private static final int KEEP_ALIVE_TIMEOUT_SECONDS = 30;

    private Server grpcServer;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final Map<String, ManagedChannel> activeChannels = new ConcurrentHashMap<>();

    @Bean
    public Server grpcServer(HealthService healthService) throws Exception {
        ServerInterceptor loggingInterceptor = new GrpcLoggingInterceptor();
        ServerInterceptor metricsInterceptor = new GrpcMetricsInterceptor();

        grpcServer = ServerBuilder.forPort(GRPC_PORT)
            .addService(ServerInterceptors.intercept(healthService, loggingInterceptor, metricsInterceptor))
            .addService(ProtoReflectionService.newInstance())
            .executor(executorService)
            .maxInboundMessageSize(MAX_INBOUND_MESSAGE_SIZE_MB * 1024 * 1024)
            .maxOutboundMessageSize(MAX_OUTBOUND_MESSAGE_SIZE_MB * 1024 * 1024)
            .keepAliveTime(KEEP_ALIVE_TIME_SECONDS, TimeUnit.SECONDS)
            .keepAliveTimeout(KEEP_ALIVE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .permitKeepAliveWithoutCalls(true)
            .build();

        return grpcServer;
    }

    @Bean
    public HealthService healthService() {
        return HealthService.newInstance(new HealthService.HealthServiceImpl() {
            @Override
            public void check(HealthCheckRequest request, StreamObserver<HealthCheckResponse> responseObserver) {
                HealthCheckResponse response = HealthCheckResponse.newBuilder()
                    .setStatus(HealthCheckResponse.ServingStatus.SERVING)
                    .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }

            @Override
            public void watch(HealthCheckRequest request, StreamObserver<HealthCheckResponse> responseObserver) {
                HealthCheckResponse response = HealthCheckResponse.newBuilder()
                    .setStatus(HealthCheckResponse.ServingStatus.SERVING)
                    .build();
                responseObserver.onNext(response);
            }
        });
    }

    @Bean
    public ManagedChannel grpcChannel() {
        return ManagedChannelBuilder.forAddress("localhost", GRPC_PORT)
            .usePlaintext()
            .maxInboundMessageSize(MAX_MESSAGE_SIZE_MB * 1024 * 1024)
            .maxOutboundMessageSize(MAX_MESSAGE_SIZE_MB * 1024 * 1024)
            .keepAliveTime(KEEP_ALIVE_TIME_SECONDS, TimeUnit.SECONDS)
            .keepAliveTimeout(KEEP_ALIVE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build();
    }

    @Bean
    public Function<String, ManagedChannel> grpcChannelFactory() {
        return host -> activeChannels.computeIfAbsent(host, h -> {
            log.info("Creating gRPC channel to host: {}", h);
            return ManagedChannelBuilder.forAddress(h, GRPC_PORT)
                .usePlaintext()
                .build();
        });
    }

    @Bean
    public GrpcMessageService grpcMessageService() {
        return new GrpcMessageService();
    }

    @Override
    public void afterAfterBean() throws Exception {
        if (grpcServer != null) {
            grpcServer.start();
            log.info("gRPC server started on port {}", GRPC_PORT);
        }
    }

    @Override
    public void destroy() throws Exception {
        if (grpcServer != null) {
            grpcServer.shutdown();
            log.info("gRPC server shutdown initiated");
        }
        executorService.shutdown();
        activeChannels.values().forEach(channel -> {
            try {
                channel.shutdown();
            } catch (Exception e) {
                log.warn("Error closing channel", e);
            }
        });
        log.info("gRPC resources cleaned up");
    }

    public int getActiveChannelCount() {
        return activeChannels.size();
    }

    public Server getGrpcServer() {
        return grpcServer;
    }

    static class GrpcLoggingInterceptor implements ServerInterceptor {
        private static final Logger log = LoggerFactory.getLogger(GrpcLoggingInterceptor.class);

        @Override
        public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
                ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
            log.info("gRPC call: {}", call.getMethodDescriptor().getFullMethodName());
            return new ServerCall.Listener<>() {
                @Override
                public void onMessage(ReqT message) {
                    log.debug("Received message: {}", message);
                    super.onMessage(message);
                }

                @Override
                public void onComplete() {
                    log.debug("Call completed: {}", call.getMethodDescriptor().getFullMethodName());
                    super.onComplete();
                }

                @Override
                public void onCancel() {
                    log.warn("Call cancelled: {}", call.getMethodDescriptor().getFullMethodName());
                    super.onCancel();
                }
            };
        }
    }

    static class GrpcMetricsInterceptor implements ServerInterceptor {
        private static final Logger log = LoggerFactory.getLogger(GrpcMetricsInterceptor.class);
        private final Map<String, Long> methodCallCounts = new ConcurrentHashMap<>();

        @Override
        public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
                ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
            String methodName = call.getMethodDescriptor().getFullMethodName();
            methodCallCounts.merge(methodName, 1L, Long::sum);
            log.debug("Method {} called {} times", methodName, methodCallCounts.get(methodName));
            return next.startCall(call, headers);
        }

        public Map<String, Long> getMethodCallCounts() {
            return Map.copyOf(methodCallCounts);
        }
    }

    public static class GrpcMessageService {
        private static final Logger log = LoggerFactory.getLogger(GrpcMessageService.class);

        public Mono<String> processMessage(String payload) {
            return Mono.fromCallable(() -> {
                log.info("Processing gRPC message: {}", payload);
                return "{\"result\":\"processed\",\"data\":\"" + payload + "\"}";
            }).subscribeOn(Schedulers.boundedElastic());
        }

        public Flux<String> processMessageStream(String payload, int count) {
            return Flux.range(0, count)
                .delayElements(java.time.Duration.ofMillis(50))
                .map(i -> "{\"sequence\":" + i + ",\"data\":\"" + payload + "\"}")
                .subscribeOn(Schedulers.boundedElastic());
        }

        public Mono<String> healthCheck() {
            return Mono.just("{\"status\":\"healthy\",\"service\":\"grpc\"}");
        }
    }
}