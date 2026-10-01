# AGENTS.md

Instrucciones para el agente de IA que abra este repositorio (Claude Code, Cursor, Codex, Copilot, Gemini). Se cargan solas: no hay que pegar nada en ningun chat.

## Que es este repositorio

Es el codigo base de un reto de aprendizaje de Pragma: **Comparación y aplicación de protocolos de comunicación avanzados**.

| | |
|---|---|
| Tema | Protocolos de comunicación no convencionales |
| Nivel | advanced-l2 |
| Chapter | Integración — Desarrollo |
| Especialidad | Api |
| Stack | Java / Spring Boot 3.5 |
| Patron arquitectonico | microservicio reactivo con capas estándar (API, dominio, infraestructura) |
| Tiempo estimado | 6 horas |

## Receta del stack

Esqueleto obligatorio:

- `openapi.yaml con paths, schemas y responses completos`
- `policies/ con las politicas del gateway (rate limit, auth, transformacion)`
- `environments/ con la configuracion por ambiente`
- `tests/ con la coleccion de contrato`
- `README.md con el contrato y los codigos de error`

Dependencias:

- org.springframework.boot:spring-boot-starter-webflux 3.5.6
- org.springframework.boot:spring-boot-starter-rsocket 3.5.6
- io.rsocket:rsocket-core 1.1.3
- io.grpc:grpc-netty 1.62.2
- io.grpc:grpc-protobuf 1.62.2
- io.grpc:grpc-stub 1.62.2
- net.devh:grpc-server-spring-boot-starter 2.15.0.RELEASE
- org.springframework.boot:spring-boot-starter-amqp 3.5.6
- org.springframework.graphql:spring-graphql 1.2.6
- io.projectreactor:reactor-core 3.5.13
- org.springframework.boot:spring-boot-starter-test 3.5.6
- io.projectreactor:reactor-test 3.5.13
- org.testcontainers:junit-jupiter 1.19.7
- org.testcontainers:rabbitmq 1.19.7

## Tu tarea

Dejar este proyecto en estado **verificable**: que el comando de verificacion corra sin errores. Escribi los archivos en disco, en este repositorio. No generes ZIPs ni archivos adjuntos.

En orden:

1. Corre `npx --yes @redocly/cli lint openapi.yaml` y mira que falla.
2. Completa lo que falte de la lista de abajo: manifiesto de dependencias, punto de entrada, capa de interfaz y las capas del patron declarado.
3. Arregla SOLO los errores que impiden compilar o arrancar.
4. Volve a correr `npx --yes @redocly/cli lint openapi.yaml` hasta que pase.
5. Pará ahí.

## Regla dura: las fases son trabajo del humano

**PROHIBIDO implementar los entregables de las fases.** El valor del reto esta en que la persona los resuelva. Tu trabajo es que tenga un proyecto que arranca; el hueco pedagogico se queda como esta.

No resuelvas nada de esto:

- **Fase 1 — Comparación de protocolos de comunicación**: Resumen comparativo de RSocket vs HTTP/1, HTTP/2 y WebSocket.
- **Fase 2 — Aplicación de gRPC en microservicios**: Informe sobre la aplicación de gRPC en microservicios.
- **Fase 3 — Manejo de mensajes y colas en AMQP**: Informe sobre el manejo de mensajes y colas en AMQP.

Distincion operativa:

- **Arreglar** (si): import faltante, tipo que no existe, dependencia sin declarar, error de sintaxis, archivo referenciado que no existe.
- **No tocar** (no): logica de negocio incompleta, validaciones ausentes, secretos hardcodeados, APIs deprecadas que funcionan, concurrencia insegura, patrones mejorables. Eso es lo que la persona tiene que encontrar.

## Lo que falta y tenes que completar

### 1. Archivos que la arquitectura declara (2 de 18)

La propuesta arquitectonica del reto los lista y no llegaron al repo. Crealos con implementacion real, respetando la capa en la que viven:

- [ ] `src/test/java/com/integracion/protocolos/api/GrpcServiceTest.java`
- [ ] `src/test/java/com/integracion/protocolos/infrastructure/AmqpProducerConsumerTest.java`

### 2. Referencias colgando (60)

Salieron de un analisis estatico del codigo que SI esta en el repo. Cada una rompe la compilacion:

- [ ] `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `com.integracion.protocolos.proto.ProcessRequest`
      El import com.integracion.protocolos.proto.ProcessRequest usa un paquete propio del proyecto pero ningun archivo generado declara ese tipo. Falta generar la clase/interfaz, o el import esta mal escrito.
- [ ] `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `com.integracion.protocolos.proto.ProcessResponse`
      El import com.integracion.protocolos.proto.ProcessResponse usa un paquete propio del proyecto pero ningun archivo generado declara ese tipo. Falta generar la clase/interfaz, o el import esta mal escrito.
- [ ] `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `com.integracion.protocolos.proto.StreamRequest`
      El import com.integracion.protocolos.proto.StreamRequest usa un paquete propio del proyecto pero ningun archivo generado declara ese tipo. Falta generar la clase/interfaz, o el import esta mal escrito.
- [ ] `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `com.integracion.protocolos.proto.StreamResponse`
      El import com.integracion.protocolos.proto.StreamResponse usa un paquete propio del proyecto pero ningun archivo generado declara ese tipo. Falta generar la clase/interfaz, o el import esta mal escrito.
- [ ] `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `com.integracion.protocolos.proto.ServiceGrpc`
      El import com.integracion.protocolos.proto.ServiceGrpc usa un paquete propio del proyecto pero ningun archivo generado declara ese tipo. Falta generar la clase/interfaz, o el import esta mal escrito.
- [ ] `src/main/java/com/integracion/protocolos/ProtocolosApplication.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Hooks pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/domain/MessageService.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/domain/MessageService.java` — `reactor.util.retry`
      El import reactor.util.retry.Retry pertenece a reactor.util.retry, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/api/RSocketController.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/api/RSocketController.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `reactor.core.scheduler`
      El import reactor.core.scheduler.Schedulers pertenece a reactor.core.scheduler, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/infrastructure/AmqpProducer.java` — `com.fasterxml.jackson`
      El import com.fasterxml.jackson.core.JsonProcessingException pertenece a com.fasterxml.jackson, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/infrastructure/AmqpProducer.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/infrastructure/AmqpProducer.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/infrastructure/AmqpConsumer.java` — `com.fasterxml.jackson`
      El import com.fasterxml.jackson.databind.ObjectMapper pertenece a com.fasterxml.jackson, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/infrastructure/AmqpConsumer.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/infrastructure/AmqpConsumer.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/config/RSocketConfig.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/config/RSocketConfig.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/config/RSocketConfig.java` — `reactor.core.scheduler`
      El import reactor.core.scheduler.Schedulers pertenece a reactor.core.scheduler, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/config/RSocketConfig.java` — `reactor.netty.resources`
      El import reactor.netty.resources.LoopResources pertenece a reactor.netty.resources, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/config/GrpcConfig.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/config/GrpcConfig.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/config/GrpcConfig.java` — `reactor.core.scheduler`
      El import reactor.core.scheduler.Schedulers pertenece a reactor.core.scheduler, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `graphql.schema`
      El import graphql.schema.DataFetcher pertenece a graphql.schema, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `graphql.schema.idl`
      El import graphql.schema.idl.RuntimeWiring pertenece a graphql.schema.idl, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/test/java/com/integracion/protocolos/api/RSocketControllerTest.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/integracion/protocolos/api/RSocketController.java` — `MessageModel.messageId`
      Se invoca `messageId` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/api/RSocketController.java` — `MessageModel.payload`
      Se invoca `payload` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `MessageModel.messageId`
      Se invoca `messageId` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `MessageModel.payload`
      Se invoca `payload` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `MessageModel.status`
      Se invoca `status` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `MessageModel.timestamp`
      Se invoca `timestamp` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/infrastructure/AmqpConsumer.java` — `MessageModel.status`
      Se invoca `status` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/infrastructure/AmqpConsumer.java` — `MessageModel.payload`
      Se invoca `payload` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/infrastructure/AmqpConsumer.java` — `MessageModel.correlationId`
      Se invoca `correlationId` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/infrastructure/AmqpConsumer.java` — `MessageModel.retryCount`
      Se invoca `retryCount` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `MessageInput.payload`
      Se invoca `payload` sobre `MessageInput`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `MessageInput.targetService`
      Se invoca `targetService` sobre `MessageInput`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `MessageModel.correlationId`
      Se invoca `correlationId` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `MessageModel.payload`
      Se invoca `payload` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `MessageModel.status`
      Se invoca `status` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `MessageModel.timestamp`
      Se invoca `timestamp` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `MessageModel.targetService`
      Se invoca `targetService` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `MessageModel.retryCount`
      Se invoca `retryCount` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `MessageModel.payload`
      Se invoca `payload` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `MessageModel.status`
      Se invoca `status` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `MessageModel.createdAt`
      Se invoca `createdAt` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `MessageModel.processedAt`
      Se invoca `processedAt` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `MessageModel.retryCount`
      Se invoca `retryCount` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `MessageModel.targetService`
      Se invoca `targetService` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `MessageModel.errorMessage`
      Se invoca `errorMessage` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `MessageModel.metadata`
      Se invoca `metadata` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/integracion/protocolos/api/RSocketControllerTest.java` — `MessageModel.messageId`
      Se invoca `messageId` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.

### Presentes (18)

- `pom.xml`
- `src/main/java/com/integracion/protocolos/ProtocolosApplication.java`
- `src/main/resources/application.yml`
- `src/main/java/com/integracion/protocolos/domain/MessageModel.java`
- `src/main/java/com/integracion/protocolos/domain/MessageService.java`
- `openapi.yaml`
- `src/main/java/com/integracion/protocolos/api/RSocketController.java`
- `src/main/java/com/integracion/protocolos/api/GrpcService.java`
- `src/main/proto/service.proto`
- `src/main/java/com/integracion/protocolos/infrastructure/AmqpProducer.java`
- `src/main/java/com/integracion/protocolos/infrastructure/AmqpConsumer.java`
- `src/main/java/com/integracion/protocolos/api/GraphQLController.java`
- `src/main/resources/graphql/schema.graphqls`
- `src/main/java/com/integracion/protocolos/config/RSocketConfig.java`
- `src/main/java/com/integracion/protocolos/config/GrpcConfig.java`
- `src/main/java/com/integracion/protocolos/config/AmqpConfig.java`
- `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java`
- `src/test/java/com/integracion/protocolos/api/RSocketControllerTest.java`

### Capas del patron declarado

Cada una tiene que existir como directorio real con al menos un archivo. Codigo plano en la raiz no satisface el patron.

- `src/main/java/com/integracion/protocolos`
- `src/main/java/com/integracion/protocolos/api`
- `src/main/java/com/integracion/protocolos/domain`
- `src/main/java/com/integracion/protocolos/infrastructure`
- `src/main/java/com/integracion/protocolos/config`
- `src/main/resources`
- `src/test/java/com/integracion/protocolos`

## Verificacion

```bash
npx --yes @redocly/cli lint openapi.yaml
```

Ese comando pasando es la definicion de "terminado" para vos.

## Convenciones que tenes que respetar

- Un solo ecosistema: no declares librerias de otro lenguaje ni mezcles gestores de paquetes.
- Toda libreria que uses tiene que estar declarada en el manifiesto de dependencias.
- Todo import declarado tiene que usarse; todo tipo usado tiene que existir o venir de una dependencia declarada.
- El patron es **microservicio reactivo con capas estándar (API, dominio, infraestructura)**: los contratos (interfaces, puertos) los define la capa interna y los implementa la externa, nunca al revés.
- Los archivos que crees llevan implementacion real, no stubs: sin `TODO`, sin cuerpos vacios, sin `// getters y setters`.

## Contexto del candidato

Sirve para calibrar el nivel del codigo, no para resolver las fases.

- Perfil: Chapter Integración, Especialidad Desarrollador, Tecnología API, Advanced
- Brecha que el reto ataca: Mostrar cómo RSocket se diferencia de HTTP/1, HTTP/2 y WebSocket. Aplicado el protocolo gRPC facilita la comunicación entre servicios en una arquitectura de microservicios. ¿Cómo se manejan los mensajes y la cola en AMQP? Explicar el rendimiento y flexibilidad al usar GraphQL en comparación con REST?
- Mision: Candidato con experiencia avanzada en API, trabaja en arquitecturas de integración

---

*Generado por Challenge Generator — Pragma. `README.md` tiene el enunciado completo del reto para la persona. `PROMPT_MEJORA.md` es la variante para pegar en un chat, si se prefiere ese flujo.*
