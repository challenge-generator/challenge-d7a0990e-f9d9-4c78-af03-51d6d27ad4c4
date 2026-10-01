package com.integracion.protocolos.config;


import com.integracion.protocolos.api.MessageInput;
import com.integracion.protocolos.domain.MessageModel;
import com.integracion.protocolos.domain.MessageModel.MessageStatus;
import com.integracion.protocolos.domain.MessageService;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.graphql.ExecutionGraphQlService;
import org.springframework.graphql.server.WebGraphQlHandler;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.webmvc.GraphQlHttpHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Configuration
public class GraphQLConfig {

    private static final Logger log = LoggerFactory.getLogger(GraphQLConfig.class);
    
    @Value("classpath:graphql/schema.graphqls")
    private Resource schemaResource;

    private final MessageService messageService;

    public GraphQLConfig(MessageService messageService) {
        this.messageService = messageService;
    }

    @Bean
    public GraphQLSchema graphQLSchema() {
        String schemaContent = """
            type Query {
                message(id: ID!): MessageModel
                messages(status: MessageStatus, limit: Int): [MessageModel!]!
                messagesByService(serviceName: String!): [MessageModel!]!
            }
            
            type Mutation {
                processMessage(input: MessageInput!): MessageModel!
                retryMessage(id: ID!): MessageModel!
            }
            
            type Subscription {
                messageProcessed: MessageModel!
                messagesByStatus(status: MessageStatus!): MessageModel!
            }
            
            input MessageInput {
                payload: String!
                targetService: String
                metadata: String
            }
            
            type MessageModel {
                id: ID!
                payload: String!
                status: MessageStatus!
                createdAt: String!
                processedAt: String
                retryCount: Int!
                targetService: String
                errorMessage: String
                metadata: String
            }
            
            enum MessageStatus {
                PENDING
                PROCESSING
                COMPLETED
                FAILED
                IN_DLQ
            }
            """;
        
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeRegistry = schemaParser.parse(schemaContent);
        
        RuntimeWiring.Builder runtimeWiringBuilder = RuntimeWiring.newRuntimeWiring();
        
        runtimeWiringBuilder.type("Query", typeWiring -> {
            typeWiring.dataFetcher("message", environment -> {
                String id = environment.getArgument("id");
                return getMessageById(id);
            });
            typeWiring.dataFetcher("messages", environment -> {
                String statusArg = environment.getArgument("status");
                Integer limit = environment.getArgument("limit");
                MessageStatus status = statusArg != null ? MessageStatus.valueOf(statusArg) : null;
                return getMessages(status, limit != null ? limit : 100);
            });
            return typeWiring;
        });
        
        runtimeWiringBuilder.type("Mutation", typeWiring -> {
            typeWiring.dataFetcher("processMessage", environment -> {
                Map<String, Object> input = environment.getArgument("input");
                return processMessageMutation(input);
            });
            typeWiring.dataFetcher("retryMessage", environment -> {
                String id = environment.getArgument("id");
                return retryMessageMutation(id);
            });
            return typeWiring;
        });
        
        runtimeWiringBuilder.type("Subscription", typeWiring -> {
            typeWiring.dataFetcher("messageProcessed", environment -> {
                return Flux.interval(java.time.Duration.ofMillis(100))
                        .take(1)
                        .map(tick -> createSampleMessage());
            });
            return typeWiring;
        });
        
        runtimeWiringBuilder.scalar(graphql.schema.GraphQLScalarType.newScalar()
                .name("DateTime")
                .coercing(new graphql.schema.Coercing<LocalDateTime, String>() {
                    @Override
                    public String serialize(Object dataFetcherResult) {
                        if (dataFetcherResult instanceof LocalDateTime) {
                            return ((LocalDateTime) dataFetcherResult).toString();
                        }
                        return null;
                    }
                    
                    @Override
                    public LocalDateTime parseValue(Object input) {
                        return LocalDateTime.parse(input.toString());
                    }
                    
                    @Override
                    public LocalDateTime parseLiteral(Object input) {
                        return LocalDateTime.parse(input.toString());
                    }
                })
                .build());
        
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiringBuilder.build());
    }

    @Bean
    public GraphQL graphQL(GraphQLSchema graphQLSchema) {
        return GraphQL.newGraphQL(graphQLSchema)
                .preparsingExecutionStrategy((executionInput, next) -> {
                    log.info("GraphQL query: {}", executionInput.getQuery());
                    return next.execute(executionInput);
                })
                .build();
    }

    @Bean
    public WebGraphQlHandler webGraphQlHandler(GraphQL graphQL) {
        return WebGraphQlHandler.builder(graphQL)
                .interceptor((request, response, next) -> {
                    HttpHeaders headers = request.getHeaders();
                    String traceId = headers.getFirst("X-Trace-Id");
                    if (traceId != null) {
                        log.debug("Trace ID: {}", traceId);
                    }
                    return next.handle(request, response);
                })
                .build();
    }

    @Bean
    public GraphQlHttpHandler graphQlHttpHandler(WebGraphQlHandler webGraphQlHandler) {
        return new GraphQlHttpHandler(webGraphQlHandler);
    }

    private MessageModel getMessageById(String id) {
        MessageModel sample = createSampleMessage();
        return new MessageModel(
                id,
                sample.payload(),
                sample.status(),
                sample.createdAt(),
                sample.processedAt(),
                sample.retryCount(),
                sample.targetService(),
                sample.errorMessage(),
                sample.metadata()
        );
    }

    private List<MessageModel> getMessages(MessageStatus status, int limit) {
        List<MessageModel> messages = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, 5); i++) {
            messages.add(createSampleMessage());
        }
        if (status != null) {
            return messages.stream()
                    .filter(m -> m.status() == status)
                    .toList();
        }
        return messages;
    }

    private MessageModel processMessageMutation(Map<String, Object> input) {
        String payload = (String) input.get("payload");
        String targetService = (String) input.get("targetService");
        String metadata = (String) input.get("metadata");
        
        MessageModel message = new MessageModel(
                UUID.randomUUID().toString(),
                payload,
                MessageStatus.PENDING,
                LocalDateTime.now(),
                null,
                0,
                targetService != null ? targetService : "default-service",
                null,
                metadata
        );
        
        return message;
    }

    private MessageModel retryMessageMutation(String id) {
        MessageModel sample = createSampleMessage();
        return new MessageModel(
                id,
                sample.payload(),
                MessageStatus.PENDING,
                sample.createdAt(),
                null,
                sample.retryCount() + 1,
                sample.targetService(),
                null,
                sample.metadata()
        );
    }

    private MessageModel createSampleMessage() {
        LocalDateTime now = LocalDateTime.now();
        return new MessageModel(
                UUID.randomUUID().toString(),
                "Sample payload for GraphQL",
                MessageStatus.COMPLETED,
                now.minusHours(1),
                now,
                0,
                "grpc-service",
                null,
                "{}"
        );
    }
}