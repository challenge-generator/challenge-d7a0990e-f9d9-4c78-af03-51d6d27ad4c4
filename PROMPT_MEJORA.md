# Prompt para Mejorar el Codigo Base

Copia y pega el contenido del bloque de abajo en un asistente de IA (Claude, ChatGPT)
para obtener un ZIP con el proyecto completo y arrancable.

Si preferis trabajar en tu editor con un agente local (Claude Code, Cursor, Copilot), usa `AGENTS.md` en vez de este archivo: dice lo mismo pero para que escriba los archivos en disco.

## Las dos reglas que no se negocian

1. **Completa el boilerplate.** Todo lo que el proyecto necesita para compilar y arrancar: manifiesto de dependencias, punto de entrada, configuracion, capa de interfaz, y las capas del patron arquitectonico declarado. Eso es andamiaje y es tu trabajo.
2. **NO resuelvas el reto.** Los entregables de las fases son el trabajo de la persona. El hueco pedagogico se deja como esta: el proyecto arranca, pero lo que el reto pide implementar NO esta implementado.

Dicho de otra forma: si algo impide compilar, arreglalo. Si algo es logica de negocio incompleta, validaciones ausentes, un secreto hardcodeado o un patron mejorable, dejalo exactamente como esta — es lo que la persona tiene que encontrar.

## Lo que le falta a este proyecto

Esto NO lo tenes que adivinar: salio de comparar el proyecto contra la arquitectura declarada del reto y de un analisis estatico del codigo. Completalo TODO.

### Archivos que la arquitectura del reto declara y no estan

Creálos con implementacion real, en la capa que les corresponde:

- `src/test/java/com/integracion/protocolos/api/GrpcServiceTest.java`
- `src/test/java/com/integracion/protocolos/infrastructure/AmqpProducerConsumerTest.java`

### Referencias colgando en el codigo que si esta

Cada una rompe la compilacion:

- `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `com.integracion.protocolos.proto.ProcessRequest`: El import com.integracion.protocolos.proto.ProcessRequest usa un paquete propio del proyecto pero ningun archivo generado declara ese tipo. Falta generar la clase/interfaz, o el import esta mal escrito.
- `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `com.integracion.protocolos.proto.ProcessResponse`: El import com.integracion.protocolos.proto.ProcessResponse usa un paquete propio del proyecto pero ningun archivo generado declara ese tipo. Falta generar la clase/interfaz, o el import esta mal escrito.
- `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `com.integracion.protocolos.proto.StreamRequest`: El import com.integracion.protocolos.proto.StreamRequest usa un paquete propio del proyecto pero ningun archivo generado declara ese tipo. Falta generar la clase/interfaz, o el import esta mal escrito.
- `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `com.integracion.protocolos.proto.StreamResponse`: El import com.integracion.protocolos.proto.StreamResponse usa un paquete propio del proyecto pero ningun archivo generado declara ese tipo. Falta generar la clase/interfaz, o el import esta mal escrito.
- `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `com.integracion.protocolos.proto.ServiceGrpc`: El import com.integracion.protocolos.proto.ServiceGrpc usa un paquete propio del proyecto pero ningun archivo generado declara ese tipo. Falta generar la clase/interfaz, o el import esta mal escrito.
- `src/main/java/com/integracion/protocolos/ProtocolosApplication.java` — `reactor.core.publisher`: El import reactor.core.publisher.Hooks pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/domain/MessageService.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/domain/MessageService.java` — `reactor.util.retry`: El import reactor.util.retry.Retry pertenece a reactor.util.retry, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/api/RSocketController.java` — `org.slf4j`: El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/api/RSocketController.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `org.slf4j`: El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `reactor.core.scheduler`: El import reactor.core.scheduler.Schedulers pertenece a reactor.core.scheduler, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/infrastructure/AmqpProducer.java` — `com.fasterxml.jackson`: El import com.fasterxml.jackson.core.JsonProcessingException pertenece a com.fasterxml.jackson, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/infrastructure/AmqpProducer.java` — `org.slf4j`: El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/infrastructure/AmqpProducer.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/infrastructure/AmqpConsumer.java` — `com.fasterxml.jackson`: El import com.fasterxml.jackson.databind.ObjectMapper pertenece a com.fasterxml.jackson, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/infrastructure/AmqpConsumer.java` — `org.slf4j`: El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/infrastructure/AmqpConsumer.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `org.slf4j`: El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/config/RSocketConfig.java` — `org.slf4j`: El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/config/RSocketConfig.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/config/RSocketConfig.java` — `reactor.core.scheduler`: El import reactor.core.scheduler.Schedulers pertenece a reactor.core.scheduler, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/config/RSocketConfig.java` — `reactor.netty.resources`: El import reactor.netty.resources.LoopResources pertenece a reactor.netty.resources, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/config/GrpcConfig.java` — `org.slf4j`: El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/config/GrpcConfig.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/config/GrpcConfig.java` — `reactor.core.scheduler`: El import reactor.core.scheduler.Schedulers pertenece a reactor.core.scheduler, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `graphql.schema`: El import graphql.schema.DataFetcher pertenece a graphql.schema, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `graphql.schema.idl`: El import graphql.schema.idl.RuntimeWiring pertenece a graphql.schema.idl, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `org.slf4j`: El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/test/java/com/integracion/protocolos/api/RSocketControllerTest.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/integracion/protocolos/api/RSocketController.java` — `MessageModel.messageId`: Se invoca `messageId` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/api/RSocketController.java` — `MessageModel.payload`: Se invoca `payload` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `MessageModel.messageId`: Se invoca `messageId` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `MessageModel.payload`: Se invoca `payload` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `MessageModel.status`: Se invoca `status` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/api/GrpcService.java` — `MessageModel.timestamp`: Se invoca `timestamp` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/infrastructure/AmqpConsumer.java` — `MessageModel.status`: Se invoca `status` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/infrastructure/AmqpConsumer.java` — `MessageModel.payload`: Se invoca `payload` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/infrastructure/AmqpConsumer.java` — `MessageModel.correlationId`: Se invoca `correlationId` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/infrastructure/AmqpConsumer.java` — `MessageModel.retryCount`: Se invoca `retryCount` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `MessageInput.payload`: Se invoca `payload` sobre `MessageInput`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `MessageInput.targetService`: Se invoca `targetService` sobre `MessageInput`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `MessageModel.correlationId`: Se invoca `correlationId` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `MessageModel.payload`: Se invoca `payload` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `MessageModel.status`: Se invoca `status` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `MessageModel.timestamp`: Se invoca `timestamp` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `MessageModel.targetService`: Se invoca `targetService` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/api/GraphQLController.java` — `MessageModel.retryCount`: Se invoca `retryCount` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `MessageModel.payload`: Se invoca `payload` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `MessageModel.status`: Se invoca `status` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `MessageModel.createdAt`: Se invoca `createdAt` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `MessageModel.processedAt`: Se invoca `processedAt` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `MessageModel.retryCount`: Se invoca `retryCount` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `MessageModel.targetService`: Se invoca `targetService` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `MessageModel.errorMessage`: Se invoca `errorMessage` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/integracion/protocolos/config/GraphQLConfig.java` — `MessageModel.metadata`: Se invoca `metadata` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/integracion/protocolos/api/RSocketControllerTest.java` — `MessageModel.messageId`: Se invoca `messageId` sobre `MessageModel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.

## Como saber que terminaste

```bash
npx --yes @redocly/cli lint openapi.yaml
```

Ese comando corriendo sin errores es la definicion de "listo".

---

```
## Briefing del reto (autoridad)
Este bloque manda sobre los archivos adjuntos. El stack y el rol salen de AQUÍ, no de un topic genérico ni de markdown placeholder.

### Perfil
Chapter Integración, Especialidad Desarrollador, Tecnología API, Advanced

### Brecha de conocimiento
Mostrar cómo RSocket se diferencia de HTTP/1, HTTP/2 y WebSocket. Aplicado el protocolo gRPC facilita la comunicación entre servicios en una arquitectura de microservicios. ¿Cómo se manejan los mensajes y la cola en AMQP? Explicar el rendimiento y flexibilidad al usar GraphQL en comparación con REST?

### Misión / candidato
Candidato con experiencia avanzada en API, trabaja en arquitecturas de integración

### Reto
- Tema: Protocolos de comunicación no convencionales
- Seniority: advanced-l2
- Tipo: theoretical
- Título: Comparación y aplicación de protocolos de comunicación avanzados
- Tiempo estimado: 6 horas

### Fases (trabajo del HUMANO — PROHIBIDO completarlas)
No implementes estos entregables. Dejalos como hueco pedagógico. El asistente solo materializa el proyecto arrancable para que el participante pueda trabajar.
- Fase 1: Comparación de protocolos de comunicación — objetivo: Entender las diferencias y ventajas de RSocket frente a HTTP/1, HTTP/2 y WebSocket. — entregable (NO resolver): Resumen comparativo de RSocket vs HTTP/1, HTTP/2 y WebSocket.
- Fase 2: Aplicación de gRPC en microservicios — objetivo: Entender cómo gRPC facilita la comunicación entre servicios en una arquitectura de microservicios. — entregable (NO resolver): Informe sobre la aplicación de gRPC en microservicios.
- Fase 3: Manejo de mensajes y colas en AMQP — objetivo: Entender cómo se manejan los mensajes y las colas en AMQP. — entregable (NO resolver): Informe sobre el manejo de mensajes y colas en AMQP.

Eres un asistente experto en análisis, corrección y generación de archivos de cualquier tipo:
código fuente, documentación, hojas de cálculo, documentos Word, configuraciones, entre otros.
Voy a enviarte una cadena de texto que contiene uno o más archivos. Cada archivo está delimitado por un marcador con el siguiente formato:
// === ARCHIVO: ruta/del/archivo.extension ===
o también puede aparecer como:
## === ARCHIVO: ruta/del/archivo.extension ===
Lo que sigue al marcador puede ser:

El contenido real del archivo (código, texto, YAML, etc.)
Una descripción en lenguaje natural de lo que debe contener el archivo


TU TAREA
PASO 0 — ¿Esto es un proyecto o una carcasa?
Antes de extraer archivos, leé el Briefing (si está) y diagnosticá el adjunto.

Es CARCASA si ocurre CUALQUIERA de estas:
- No hay manifiesto de dependencias del stack del briefing (manifest.json de VTEX IO / package.json / pom.xml / build.gradle / requirements.txt / go.mod / *.tf / *.csproj, según corresponda)
- Hay un "binario" que en realidad es un comentario ("no puede ser mostrado como texto plano", placeholder .fig/.docx vacío)
- Los markdowns ya completan entregables de fases posteriores ("se implementó fade-in", lista de áreas ya resuelta)

Si es CARCASA:
- MATERIALIZÁ un proyecto que arranca en el stack del briefing (VTEX IO Store Framework, Angular, Terraform, pytest, Nest, etc.). Incluí manifiesto, punto de entrada y capa de interfaz reales.
- NO copies los markdowns de "solución" como si fueran el producto. Son ruido de generación.
- NO resuelvas las fases del briefing (están marcadas PROHIBIDO). Dejá el hueco pedagógico: el flujo existe, las microinteracciones/calidad/infra que el reto pide NO están hechas.
- Después seguí al PASO 5 (ZIP).

Si es un proyecto REAL (manifiesto + código que compila o arranca):
- Seguí PASO 1 en adelante. 🔴 compilación sí. 🟡 pedagógico no.

PASO 1 — Detección y extracción
Identifica todos los archivos presentes en la cadena. Para cada archivo extrae:

Su ruta completa (ej: src/main/java/com/pragma/Service.java)
Su contenido o descripción

PASO 2 — Clasificación por tipo
Clasifica cada archivo en una de estas categorías:
A) Código fuente (Java, Python, TypeScript, JavaScript, Kotlin, etc.)
B) Configuración / documentación (YAML, properties, Markdown, JSON, txt, etc.)
C) Excel (.xlsx, .xls, .csv)
D) Word (.docx, .doc)
E) Otro tipo de archivo binario o especial
PASO 3 — Clasificación de errores en código fuente

Objetivo prioritario: que el proyecto compile. No corrijas flujo de negocio ni lógica funcional.

Antes de modificar cualquier archivo de código fuente, clasifica cada problema encontrado en una de estas dos categorías:
🔴 ERROR DE COMPILACIÓN — corregir siempre
Son errores que impiden que el proyecto arranque, sin valor pedagógico:

Import faltante o incorrecto
Clase, método o variable referenciada que no existe en ningún archivo del proyecto
Error de sintaxis
Anotación con atributos inválidos
Dependencia ausente en pom.xml, package.json, etc.
Archivo referenciado que no existe y debe ser creado con implementación mínima

→ CORREGIR estos errores.
🟡 PROBLEMA FUNCIONAL O DE CALIDAD — preservar siempre
Son problemas que no impiden compilar. Pueden ser intencionales para el aprendizaje:

Clave secreta hardcodeada ("secret", "password123")
API deprecada que funciona pero tiene reemplazo moderno
Lógica de negocio incorrecta o incompleta
Código redundante o de baja legibilidad
Falta de validaciones en flujo de negocio
Patrones de diseño incorrectos pero funcionales
Concurrencia no segura
Configuración funcional pero no óptima

→ PRESERVAR tal cual. No corregir, no mejorar, no comentar.
PASO 4 — Procesamiento según tipo de archivo
Tipo A — Código fuente
Aplica únicamente las correcciones clasificadas como 🔴 ERROR DE COMPILACIÓN.
No alteres ningún elemento clasificado como 🟡 PROBLEMA FUNCIONAL O DE CALIDAD.
Si falta un archivo referenciado, créalo con la implementación mínima necesaria para compilar.
Tipo B — Configuración / documentación
Extrae el contenido tal cual, sin modificaciones salvo errores evidentes de sintaxis
(ej: YAML mal indentado).
Tipo C — Excel (.xlsx)
Si viene con contenido real, genera el archivo respetando ese contenido.
Si viene con descripción en lenguaje natural, genera un archivo Excel funcional con:

Fila de encabezados en negrita con color de fondo distintivo
Columnas con ancho ajustado al contenido
Tipos de dato correctos por columna
Validaciones si la descripción lo indica
Hojas nombradas descriptivamente si hay más de una
Filas de ejemplo si no hay datos reales

Tipo D — Word (.docx)
Si viene con contenido real, genera el archivo respetando ese contenido.
Si viene con descripción en lenguaje natural, genera un documento Word funcional con:

Estilos de título (Título 1, Título 2) para jerarquía de secciones
Fuente legible (Calibri o equivalente), tamaño 11-12pt para cuerpo
Márgenes estándar
Tabla de contenido si tiene múltiples secciones
Tablas con encabezados en negrita si aplica

Tipo E — Otro
Genera el archivo con el contenido o estructura más apropiada según la descripción.
PASO 5 — Exportación en ZIP
Empaqueta todos los archivos en un único archivo ZIP descargable respetando exactamente
la estructura de rutas indicada por los marcadores.
El ZIP debe incluir:

Archivos de código con únicamente los errores de compilación corregidos
Archivos de configuración y documentación sin cambios
Archivos nuevos creados para resolver dependencias de compilación faltantes
Archivos Excel y Word generados desde descripción

IMPORTANTE: El ZIP debe estar listo para descargar al finalizar. No preguntes si el usuario
quiere generarlo. Simplemente genera el archivo y proporciona el enlace de descarga; No debes desplegar en el chat el resumen de lo que arreglaste al Zip, solo entregalo.

REGLAS IMPORTANTES

No omitas ningún archivo aunque no tenga errores ni modificaciones
Respeta los nombres y rutas exactas indicadas por los marcadores
Si un archivo no tiene marcador claro, infiere el nombre desde su contenido
Si la cadena contiene solo documentación, placeholders o binarios fake, NO la reproduzcas:
aplicá PASO 0 (materializar el proyecto del briefing). Reproducir la carcasa es un fallo.
No agregues texto después del enlace de descarga del ZIP
No preguntes si el usuario quiere el ZIP: simplemente generalo siempre
Si detectas que falta un archivo de configuración necesario para compilar
(pom.xml, package.json, requirements.txt, build.gradle, etc.), créalo e inclúyelo
inferiendo su contenido desde los imports y frameworks detectados en el código
Nunca corrijas problemas 🟡 aunque parezcan obvios o fáciles de mejorar.
El participante que recibirá este proyecto los debe encontrar y resolver él mismo.


INPUT
Aquí está la cadena con los archivos:

// === ARCHIVO: pom.xml ===
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.6</version>
        <relativePath/>
    </parent>

    <groupId>com.integracion.protocolos</groupId>
    <artifactId>protocolos-integracion</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>protocolos-integracion</name>
    <description>Microservicio reactivo para comparacion de protocolos de comunicacion avanzados</description>

    <properties>
        <java.version>21</java.version>
        <grpc.version>1.62.2</grpc.version>
        <protobuf.version>3.25.1</protobuf.version>
        <rsocket.version>1.1.3</rsocket.version>
        <spring-boot-starter-test.version>3.5.6</spring-boot-starter-test.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-rsocket</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.graphql</groupId>
            <artifactId>spring-graphql</artifactId>
            <version>1.2.6</version>
        </dependency>

        <!-- gRPC -->
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-netty</artifactId>
            <version>${grpc.version}</version>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-protobuf</artifactId>
            <version>${grpc.version}</version>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-stub</artifactId>
            <version>${grpc.version}</version>
        </dependency>
        <dependency>
            <groupId>net.devh</groupId>
            <artifactId>grpc-server-spring-boot-starter</artifactId>
            <version>2.15.0.RELEASE</version>
        </dependency>

        <!-- RSocket -->
        <dependency>
            <groupId>io.rsocket</groupId>
            <artifactId>rsocket-core</artifactId>
            <version>${rsocket.version}</version>
        </dependency>

        <!-- Reactor -->
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-core</artifactId>
            <version>3.5.13</version>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-test</artifactId>
            <scope>test</scope>
            <version>3.5.13</version>
        </dependency>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
            <version>1.19.7</version>
        </dependency>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>rabbitmq</artifactId>
            <scope>test</scope>
            <version>1.19.7</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.xolstice.maven.plugins</groupId>
                <artifactId>protobuf-maven-plugin</artifactId>
                <version>0.6.1</version>
                <configuration>
                    <protocArtifact>com.google.protobuf:protoc:${protobuf.version}:exe:${os.detected.classifier}</protocArtifact>
                    <pluginId>grpc-java</pluginId>
                    <pluginArtifact>io.grpc:protoc-gen-grpc-java:${grpc.version}:exe:${os.detected.classifier}</pluginArtifact>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>compile-custom</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>
</project>

// === ARCHIVO: src/main/java/com/integracion/protocolos/ProtocolosApplication.java ===
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

// === ARCHIVO: src/main/resources/application.yml ===
server:
  port: 8080
  netty:
    connection-timeout: 5000ms

spring:
  main:
    web-application-type: reactive
  rsocket:
    server:
      transport: websocket
      mapping-path: /rsocket
      port: 7000
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    virtual-host: /
    listener:
      simple:
        acknowledge-mode: manual
        prefetch: 10
        retry:
          enabled: true
          max-attempts: 3
          initial-interval: 1000ms
          multiplier: 2.0
          max-interval: 10000ms
    template:
      retry:
        enabled: true
        max-attempts: 3
  graphql:
    graphiql:
      enabled: true
    schema:
      printer:
        enabled: true
      locations: classpath:graphql/**/

grpc:
  server:
    port: 9090
    security:
      enabled: false

logging:
  level:
    org.springframework: INFO
    io.rsocket: DEBUG
    io.grpc: DEBUG
    reactor.netty: DEBUG

app:
  protocols:
    grpc:
      service-name: ProtocolService
      max-message-size: 10MB
    rsocket:
      route: protocolos.stream
    amqp:
      exchange: protocolos.exchange
      queue: protocolos.queue
      routing-key: protocolos.routingkey
    graphql:
      path: /graphql
      subscriptions-path: /subscriptions

// === ARCHIVO: src/main/java/com/integracion/protocolos/domain/MessageModel.java ===
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

// === ARCHIVO: src/main/java/com/integracion/protocolos/domain/MessageService.java ===
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

// === ARCHIVO: openapi.yaml ===
openapi: 3.1.0
info:
  title: Protocolos de Integración API
  description: API para comparación y gestión de protocolos de comunicación avanzados en arquitecturas de microservicios. Expone endpoints REST para interacción con RSocket, gRPC, AMQP y GraphQL.
  version: 1.0.0
  contact:
    name: Equipo de Integración
    email: integracion@empresa.com
  license:
    name: Apache 2.0
    url: https://www.apache.org/licenses/LICENSE-2.0.html
servers:
  - url: http://localhost:8080
    description: Servidor de desarrollo local
  - url: https://api.integracion.empresa.com
    description: Servidor de producción
tags:
  - name: Mensajería
    description: Endpoints para gestión de mensajes a través de diferentes protocolos
  - name: Protocolos
    description: Endpoints para comparación y diagnóstico de protocolos
  - name: Health
    description: Endpoints de verificación de estado del servicio
paths:
  /api/v1/messages:
    post:
      tags:
        - Mensajería
      summary: Procesar un mensaje a través del pipeline de integración
      operationId: processMessage
      requestBody:
        description: Modelo del mensaje a procesar
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/MessageModel'
            example:
              payload: "{\"tipo\":\"orden\",\"id\":12345,\"monto\":1000.50}"
              sourceService: orders-api
              targetService: inventory-service
              correlationId: corr-abc-123-def
      responses:
        '200':
          description: Mensaje procesado exitosamente
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/MessageModel'
        '400':
          description: Solicitud inválida - datos de entrada mal formados
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
        '500':
          description: Error interno al procesar el mensaje
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
  /api/v1/messages/stream:
    post:
      tags:
        - Mensajería
      summary: Procesar un stream de mensajes de forma reactiva
      operationId: processMessageStream
      requestBody:
        description: Stream de mensajes a procesar
        required: true
        content:
          application/json:
            schema:
              type: array
              items:
                $ref: '#/components/schemas/MessageModel'
      responses:
        '200':
          description: Stream de mensajes procesado exitosamente
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/MessageModel'
        '400':
          description: Solicitud inválida
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
  /api/v1/protocols/compare:
    get:
      tags:
        - Protocolos
      summary: Obtener comparación de protocolos de comunicación
      operationId: getProtocolComparison
      parameters:
        - name: protocolA
          in: query
          description: Primer protocolo a comparar
          required: false
          schema:
            type: string
            enum:
              - RSOCKET
              - GRPC
              - HTTP1
              - HTTP2
              - WEBSOCKET
              - AMQP
        - name: protocolB
          in: query
          description: Segundo protocolo a comparar
          required: false
          schema:
            type: string
            enum:
              - RSOCKET
              - GRPC
              - HTTP1
              - HTTP2
              - WEBSOCKET
              - AMQP
      responses:
        '200':
          description: Comparación de protocolos retrieved successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ProtocolComparison'
  /api/v1/protocols/rsocket/status:
    get:
      tags:
        - Protocolos
      summary: Verificar estado del servicio RSocket
      operationId: getRSocketStatus
      responses:
        '200':
          description: Estado del servicio RSocket
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ProtocolStatus'
  /api/v1/protocols/grpc/status:
    get:
      tags:
        - Protocolos
      summary: Verificar estado del servicio gRPC
      operationId: getGrpcStatus
      responses:
        '200':
          description: Estado del servicio gRPC
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ProtocolStatus'
  /api/v1/protocols/amqp/status:
    get:
      tags:
        - Protocolos
      summary: Verificar estado del broker AMQP
      operationId: getAmqpStatus
      responses:
        '200':
          description: Estado del broker AMQP
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ProtocolStatus'
  /api/v1/protocols/graphql/status:
    get:
      tags:
        - Protocolos
      summary: Verificar estado del servidor GraphQL
      operationId: getGraphQLStatus
      responses:
        '200':
          description: Estado del servidor GraphQL
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ProtocolStatus'
  /api/v1/health:
    get:
      tags:
        - Health
      summary: Verificar estado general del servicio
      operationId: healthCheck
      responses:
        '200':
          description: Servicio saludable
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/HealthResponse'
  /api/v1/health/ready:
    get:
      tags:
        - Health
      summary: Verificar disponibilidad para recibir tráfico
      operationId: readinessCheck
      responses:
        '200':
          description: Servicio listo para recibir tráfico
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/HealthResponse'
  /api/v1/health/live:
    get:
      tags:
        - Health
      summary: Verificar que el servicio está corriendo
      operationId: livenessCheck
      responses:
        '200':
          description: Servicio vivo
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/HealthResponse'
components:
  schemas:
    MessageModel:
      type: object
      description: Modelo canónico de mensaje para comunicación entre servicios
      required:
        - payload
        - sourceService
        - status
      properties:
        payload:
          type: string
          description: Contenido serializado del mensaje (JSON, XML, etc.)
          example: "{\"tipo\":\"orden\",\"id\":12345}"
        sourceService:
          type: string
          description: Identificador del servicio que origina el mensaje
          example: orders-api
        targetService:
          type: string
          description: Identificador del servicio destino del mensaje
          example: inventory-service
        correlationId:
          type: string
          description: Identificador de correlación para trazabilidad distribuida
          format: uuid
          example: 550e8400-e29b-41d4-a716-446655440000
        status:
          $ref: '#/components/schemas/MessageStatus'
        retryCount:
          type: integer
          description: Número de reintentos realizados
          minimum: 0
          default: 0
          example: 2
        timestamp:
          type: string
          description: Timestamp de creación del mensaje
          format: date-time
          example: 2024-01-15T10:30:00Z
        errorDetails:
          type: string
          description: Detalles del último error ocurrido
          nullable: true
    MessageStatus:
      type: string
      enum:
        - PENDING
        - PROCESSING
        - COMPLETED
        - FAILED
        - DLQ
      description: Estados posibles del ciclo de vida del mensaje
      example: PENDING
    ProtocolComparison:
      type: object
      description: Resultado de comparación entre dos protocolos
      properties:
        protocolA:
          type: string
          description: Nombre del primer protocolo
          example: RSOCKET
        protocolB:
          type: string
          description: Nombre del segundo protocolo
          example: GRPC
        comparisonPoints:
          type: array
          items:
            $ref: '#/components/schemas/ComparisonPoint'
    ComparisonPoint:
      type: object
      description: Punto específico de comparación entre protocolos
      properties:
        criterion:
          type: string
          description: Criterio de comparación
          example: Latencia
        valueA:
          type: string
          description: Valor o descripción para el protocolo A
          example: < 5ms
        valueB:
          type: string
          description: Valor o descripción para el protocolo B
          example: 10-50ms
        winner:
          type: string
          description: Protocolo que gana en este punto
          enum:
            - A
            - B
            - TIE
    ProtocolStatus:
      type: object
      description: Estado de un protocolo específico
      properties:
        protocol:
          type: string
          description: Nombre del protocolo
          example: RSOCKET
        available:
          type: boolean
          description: Indica si el protocolo está disponible
          example: true
        latencyMs:
          type: number
          format: double
          description: Latencia promedio en milisegundos
          example: 3.5
        activeConnections:
          type: integer
          description: Número de conexiones activas
          example: 42
        lastCheck:
          type: string
          format: date-time
          description: Timestamp del último check
          example: 2024-01-15T10:30:00Z
    HealthResponse:
      type: object
      description: Respuesta de verificación de salud del servicio
      properties:
        status:
          type: string
          enum:
            - UP
            - DOWN
            - DEGRADED
          description: Estado general del servicio
          example: UP
        timestamp:
          type: string
          format: date-time
          description: Timestamp de la verificación
          example: 2024-01-15T10:30:00Z
        services:
          type: object
          description: Estado de cada subsistema
          additionalProperties:
            $ref: '#/components/schemas/ServiceHealth'
    ServiceHealth:
      type: object
      description: Estado de un servicio específico
      properties:
        status:
          type: string
          enum:
            - UP
            - DOWN
          example: UP
        latencyMs:
          type: number
          format: double
          nullable: true
          description: Latencia del servicio
    ErrorResponse:
      type: object
      description: Estructura estándar de respuesta de error
      required:
        - error
        - message
        - timestamp
      properties:
        error:
          type: string
          description: Código de error técnico
          example: MESSAGE_PROCESSING_ERROR
        message:
          type: string
          description: Descripción legible del error
          example: Error al procesar el mensaje en el servicio destino
        timestamp:
          type: string
          format: date-time
          description: Timestamp del error
          example: 2024-01-15T10:30:00Z
        details:
          type: object
          description: Información adicional de debug
          nullable: true
        correlationId:
          type: string
          format: uuid
          description: Correlation ID para trazabilidad
          nullable: true
  securitySchemes:
    BearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
      description: Token JWT para autenticación
    ApiKeyAuth:
      type: apiKey
      in: header
      name: X-API-Key
      description: API Key para acceso a la API
security:
  - BearerAuth: []
  - ApiKeyAuth: []

// === ARCHIVO: src/main/java/com/integracion/protocolos/api/RSocketController.java ===
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

// === ARCHIVO: src/main/java/com/integracion/protocolos/api/GrpcService.java ===
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

// === ARCHIVO: src/main/proto/service.proto ===
syntax = "proto3";

option java_multiple_files = true;
option java_package = "com.integracion.protocolos.proto";
option java_outer_classname = "ServiceProto";

package com.integracion.protocolos;

message ProcessRequest {
    string request_id = 1;
    string payload = 2;
    map<string, string> metadata = 3;
    int32 priority = 4;
}

message ProcessResponse {
    string response_id = 1;
    string payload = 2;
    string status = 3;
    string timestamp = 4;
    bool success = 5;
    string error_message = 6;
}

message StreamRequest {
    string request_id = 1;
    string payload = 2;
    int32 message_count = 3;
    int32 delay_ms = 4;
}

message StreamResponse {
    string response_id = 1;
    string payload = 2;
    string status = 3;
    string timestamp = 4;
    int32 sequence_number = 5;
    bool success = 6;
}

message BatchRequest {
    repeated ProcessRequest requests = 1;
    string batch_id = 2;
    bool allow_partial_failure = 3;
}

message BatchResponse {
    repeated ProcessResponse responses = 1;
    string batch_id = 2;
    int32 success_count = 3;
    int32 failure_count = 4;
}

service Service {
    rpc ProcessMessage(ProcessRequest) returns (ProcessResponse);
    
    rpc ProcessMessageStream(StreamRequest) returns (stream StreamResponse);
    
    rpc BidirectionalStream(stream ProcessRequest) returns (stream ProcessResponse);
    
    rpc ProcessBatch(BatchRequest) returns (BatchResponse);
}

// === ARCHIVO: src/main/java/com/integracion/protocolos/infrastructure/AmqpProducer.java ===
package com.integracion.protocolos.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integracion.protocolos.domain.MessageModel;
import com.integracion.protocolos.domain.MessageModel.MessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class AmqpProducer {

    private static final Logger log = LoggerFactory.getLogger(AmqpProducer.class);
    private static final String DEFAULT_EXCHANGE = "protocolos.exchange";
    private static final String MAIN_QUEUE = "protocolos.main.queue";
    private static final String DLQ_QUEUE = "protocolos.dlq.queue";
    private static final String MAIN_ROUTING_KEY = "protocolos.message";
    private static final String DLQ_ROUTING_KEY = "protocolos.dlq";
    private static final int MAX_RETRIES = 3;

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final Sinks.Many<MessageModel> messageSink;
    private final AtomicInteger messageCounter;

    @Value("${amqp.producer.retry.enabled:true}")
    private boolean retryEnabled;

    @Value("${amqp.producer.dlq.enabled:true}")
    private boolean dlqEnabled;

    public AmqpProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.messageSink = Sinks.many().unicast().onBackpressureBuffer();
        this.messageCounter = new AtomicInteger(0);
        configureMessageConverter();
    }

    private void configureMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setTypeIdPropertyName("__typeId__");
        rabbitTemplate.setMessageConverter(converter);
    }

    public Mono<MessageModel> sendMessage(MessageModel message) {
        return Mono.fromCallable(() -> {
            String correlationId = UUID.randomUUID().toString();
            MessageModel messageWithMeta = message.withStatus(MessageStatus.SENT);
            
            Map<String, Object> headers = new HashMap<>();
            headers.put("correlationId", correlationId);
            headers.put("timestamp", System.currentTimeMillis());
            headers.put("source", "amqp-producer");
            headers.put("retryCount", 0);
            headers.put("maxRetries", MAX_RETRIES);
            
            try {
                String jsonPayload = objectMapper.writeValueAsString(messageWithMeta);
                org.springframework.amqp.core.Message amqpMessage = createAmqpMessage(jsonPayload, headers);
                
                CorrelationData correlationData = new CorrelationData(correlationId);
                rabbitTemplate.send(DEFAULT_EXCHANGE, MAIN_ROUTING_KEY, amqpMessage, correlationData);
                
                int count = messageCounter.incrementAndGet();
                log.info("Mensaje enviado exitosamente a la cola principal. CorrelationId: {}, Contador: {}", 
                        correlationId, count);
                
                messageSink.emitNext(messageWithMeta, Sinks.EmitFailureHandler.FAIL_FAST);
                return messageWithMeta;
                
            } catch (JsonProcessingException e) {
                log.error("Error al serializar el mensaje: {}", e.getMessage());
                throw new RuntimeException("Error de serialización", e);
            }
        }).doOnError(error -> {
            log.error("Error enviando mensaje: {}", error.getMessage());
            handleSendError(message, error);
        }).retryWhen(reactor.util.retry.Retry.backoff(3, Duration.ofMillis(100))
                .doBeforeRetry(retrySignal -> {
                    log.warn("Reintentando envío del mensaje. Intento: {}", retrySignal.totalRetries() + 1);
                }));
    }

    private org.springframework.amqp.core.Message createAmqpMessage(String payload, Map<String, Object> headers) {
        org.springframework.amqp.core.MessageProperties props = new org.springframework.amqp.core.MessageProperties();
        props.setContentType(org.springframework.amqp.core.MessageProperties.CONTENT_TYPE_JSON);
        props.setDeliveryMode(org.springframework.amqp.core.MessageDeliveryMode.PERSISTENT);
        props.setPriority(1);
        
        headers.forEach(props::setHeader);
        
        return new org.springframework.amqp.core.Message(payload.getBytes(), props);
    }

    private void handleSendError(MessageModel message, Throwable error) {
        log.error("Error permanente al enviar mensaje después de reintentos: {}", error.getMessage());
        if (dlqEnabled) {
            sendToDlq(message, error);
        }
    }

    private void sendToDlq(MessageModel message, Throwable error) {
        try {
            MessageModel dlqMessage = message.withStatus(MessageStatus.FAILED);
            String jsonPayload = objectMapper.writeValueAsString(dlqMessage);
            
            Map<String, Object> dlqHeaders = new HashMap<>();
            dlqHeaders.put("original-exchange", DEFAULT_EXCHANGE);
            dlqHeaders.put("original-routing-key", MAIN_ROUTING_KEY);
            dlqHeaders.put("error-message", error.getMessage());
            dlqHeaders.put("error-type", error.getClass().getName());
            dlqHeaders.put("dlq-timestamp", System.currentTimeMillis());
            
            org.springframework.amqp.core.Message dlqAmqpMessage = createAmqpMessage(jsonPayload, dlqHeaders);
            rabbitTemplate.send(DEFAULT_EXCHANGE, DLQ_ROUTING_KEY, dlqAmqpMessage);
            
            log.info("Mensaje enviado a DLQ después de fallos. CorrelationId en headers.");
            
        } catch (JsonProcessingException e) {
            log.error("Error crítico: no se pudo enviar el mensaje a la DLQ: {}", e.getMessage());
        }
    }

    public Mono<Boolean> sendBatch(java.util.List<MessageModel> messages) {
        return Mono.fromCallable(() -> {
            messages.forEach(msg -> sendMessage(msg).subscribe(
                    success -> log.debug("Mensaje del batch enviado: {}", success),
                    error -> log.error("Error en batch: {}", error.getMessage())
            ));
            return true;
        });
    }

    public Mono<Long> getMessageCount() {
        return Mono.fromCallable(() -> (long) messageCounter.get());
    }

    public Sinks.Many<MessageModel> getMessageSink() {
        return messageSink;
    }

    @Bean
    public DirectExchange exchange() {
        return ExchangeBuilder.directExchange(DEFAULT_EXCHANGE)
                .durable(true)
                .withArgument("alternate-exchange", "protocolos.ae")
                .build();
    }

    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable(MAIN_QUEUE)
                .withArgument("x-dead-letter-exchange", DEFAULT_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .withArgument("x-message-ttl", 86400000)
                .build();
    }

    @Bean
    public Queue dlqQueue() {
        return QueueBuilder.durable(DLQ_QUEUE)
                .withArgument("x-message-ttl", 604800000)
                .build();
    }

    @Bean
    public Binding mainBinding(Queue mainQueue, DirectExchange exchange) {
        return BindingBuilder.bind(mainQueue).to(exchange).with(MAIN_ROUTING_KEY);
    }

    @Bean
    public Binding dlqBinding(Queue dlqQueue, DirectExchange exchange) {
        return BindingBuilder.bind(dlqQueue).to(exchange).with(DLQ_ROUTING_KEY);
    }
}

// === ARCHIVO: src/main/java/com/integracion/protocolos/infrastructure/AmqpConsumer.java ===
package com.integracion.protocolos.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integracion.protocolos.domain.MessageModel;
import com.integracion.protocolos.domain.MessageModel.MessageStatus;
import com.integracion.protocolos.domain.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class AmqpConsumer {

    private static final Logger log = LoggerFactory.getLogger(AmqpConsumer.class);
    private static final String MAIN_QUEUE = "protocolos.main.queue";
    private static final String DLQ_QUEUE = "protocolos.dlq.queue";
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_INITIAL_INTERVAL = 1000L;
    private static final double RETRY_MULTIPLIER = 2.0;
    private static final long RETRY_MAX_INTERVAL = 10000L;

    private final MessageService messageService;
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;
    private final AtomicInteger processedCounter;
    private final AtomicInteger dlqCounter;

    @Value("${amqp.consumer.prefetch:10}")
    private int prefetchCount;

    @Value("${amqp.consumer.concurrent:5}")
    private int concurrentConsumers;

    @Value("${amqp.consumer.retry.enabled:true}")
    private boolean retryEnabled;

    public AmqpConsumer(MessageService messageService, ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        this.messageService = messageService;
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.processedCounter = new AtomicInteger(0);
        this.dlqCounter = new AtomicInteger(0);
    }

    @RabbitListener(queues = MAIN_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public Mono<Void> consumeMessage(MessageModel message) {
        String messageId = extractMessageId(message);
        int currentRetry = extractRetryCount(message);
        
        log.info("Mensaje recibido. ID: {}, Retry: {}, Status: {}", 
                messageId, currentRetry, message.status());

        return messageService.processMessage(message)
                .doOnSuccess(result -> {
                    int count = processedCounter.incrementAndGet();
                    log.info("Mensaje procesado exitosamente. ID: {}, Total procesados: {}", 
                            messageId, count);
                })
                .doOnError(error -> {
                    log.error("Error procesando mensaje ID: {}. Error: {}", messageId, error.getMessage());
                    handleProcessingError(message, error, currentRetry);
                })
                .then();
    }

    private void handleProcessingError(MessageModel message, Throwable error, int currentRetry) {
        if (retryEnabled && currentRetry < MAX_RETRIES) {
            scheduleRetry(message, currentRetry + 1);
        } else {
            moveToDlq(message, error);
        }
    }

    private void scheduleRetry(MessageModel message, int nextRetry) {
        MessageModel retryMessage = message
                .withStatus(MessageStatus.RETRY)
                .withIncrementedRetryCount();
        
        long delay = calculateBackoffDelay(nextRetry);
        
        log.info("Programando reintento {} para mensaje. Delay: {}ms", nextRetry, delay);
        
        Mono.delay(Duration.ofMillis(delay))
                .flatMap(tick -> {
                    try {
                        String jsonPayload = objectMapper.writeValueAsString(retryMessage);
                        Map<String, Object> headers = new HashMap<>();
                        headers.put("retryCount", nextRetry);
                        headers.put("retryScheduled", true);
                        
                        org.springframework.amqp.core.MessageProperties props = 
                                new org.springframework.amqp.core.MessageProperties();
                        props.setContentType(org.springframework.amqp.core.MessageProperties.CONTENT_TYPE_JSON);
                        props.setDeliveryMode(org.springframework.amqp.core.MessageDeliveryMode.PERSISTENT);
                        headers.forEach(props::setHeader);
                        
                        org.springframework.amqp.core.Message amqpMessage = 
                                new org.springframework.amqp.core.Message(jsonPayload.getBytes(), props);
                        rabbitTemplate.send("protocolos.exchange", "protocolos.message", amqpMessage);
                        
                        return Mono.just(true);
                    } catch (Exception e) {
                        log.error("Error al reprogramar mensaje: {}", e.getMessage());
                        return Mono.error(e);
                    }
                })
                .subscribe(
                        success -> log.debug("Mensaje reprogramado exitosamente"),
                        error -> log.error("Error en reprogramación: {}", error.getMessage())
                );
    }

    private long calculateBackoffDelay(int retryAttempt) {
        long delay = (long) (RETRY_INITIAL_INTERVAL * Math.pow(RETRY_MULTIPLIER, retryAttempt - 1));
        return Math.min(delay, RETRY_MAX_INTERVAL);
    }

    private void moveToDlq(MessageModel message, Throwable error) {
        try {
            MessageModel failedMessage = message.withStatus(MessageStatus.FAILED);
            String jsonPayload = objectMapper.writeValueAsString(failedMessage);
            
            Map<String, Object> dlqHeaders = new HashMap<>();
            dlqHeaders.put("original-queue", MAIN_QUEUE);
            dlqHeaders.put("error-message", error.getMessage());
            dlqHeaders.put("error-type", error.getClass().getName());
            dlqHeaders.put("final-retry-count", extractRetryCount(message));
            dlqHeaders.put("moved-to-dlq-timestamp", System.currentTimeMillis());
            
            org.springframework.amqp.core.MessageProperties props = 
                    new org.springframework.amqp.core.MessageProperties();
            props.setContentType(org.springframework.amqp.core.MessageProperties.CONTENT_TYPE_JSON);
            props.setDeliveryMode(org.springframework.amqp.core.MessageDeliveryMode.PERSISTENT);
            dlqHeaders.forEach(props::setHeader);
            
            org.springframework.amqp.core.Message dlqMessage = 
                    new org.springframework.amqp.core.Message(jsonPayload.getBytes(), props);
            
            rabbitTemplate.send("protocolos.exchange", "protocolos.dlq", dlqMessage);
            
            int count = dlqCounter.incrementAndGet();
            log.warn("Mensaje movido a DLQ. Total DLQ: {}. Error: {}", count, error.getMessage());
            
        } catch (Exception e) {
            log.error("Error crítico al mover mensaje a DLQ: {}", e.getMessage());
        }
    }

    @RabbitListener(queues = DLQ_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public Mono<Void> consumeDlqMessage(MessageModel message) {
        log.warn("Mensaje recibido de DLQ. ID: {}, Status: {}, Payload: {}", 
                extractMessageId(message), message.status(), message.payload());
        
        return Mono.empty();
    }

    private String extractMessageId(MessageModel message) {
        return message.correlationId() != null ? message.correlationId() : "unknown";
    }

    private int extractRetryCount(MessageModel message) {
        return message.retryCount() != null ? message.retryCount() : 0;
    }

    public Mono<Long> getProcessedCount() {
        return Mono.just((long) processedCounter.get());
    }

    public Mono<Long> getDlqCount() {
        return Mono.just((long) dlqCounter.get());
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(concurrentConsumers);
        factory.setMaxConcurrentConsumers(concurrentConsumers * 2);
        factory.setPrefetchCount(prefetchCount);
        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(jsonMessageConverter());
        
        factory.setAdviceChain(org.springframework.retry.interceptor.RetryInterceptorBuilder
                .stateless()
                .retryPolicy(new SimpleRetryPolicy(3))
                .backOffPolicy(createBackOffPolicy())
                .build());
        
        return factory;
    }

    private Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setTypeIdPropertyName("__typeId__");
        return converter;
    }

    private ExponentialBackOffPolicy createBackOffPolicy() {
        ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
        policy.setInitialInterval(RETRY_INITIAL_INTERVAL);
        policy.setMultiplier(RETRY_MULTIPLIER);
        policy.setMaxInterval(RETRY_MAX_INTERVAL);
        return policy;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(MAX_RETRIES);
        retryTemplate.setRetryPolicy(retryPolicy);
        
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(RETRY_INITIAL_INTERVAL);
        backOffPolicy.setMultiplier(RETRY_MULTIPLIER);
        backOffPolicy.setMaxInterval(RETRY_MAX_INTERVAL);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        
        return retryTemplate;
    }
}

// === ARCHIVO: src/main/java/com/integracion/protocolos/api/GraphQLController.java ===
package com.integracion.protocolos.api;

import com.integracion.protocolos.domain.MessageModel;
import com.integracion.protocolos.domain.MessageModel.MessageStatus;
import com.integracion.protocolos.domain.MessageService;
import com.integracion.protocolos.infrastructure.AmqpProducer;
import com.integracion.protocolos.infrastructure.AmqpConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Controller
public class GraphQLController {

    private static final Logger log = LoggerFactory.getLogger(GraphQLController.class);
    
    private final MessageService messageService;
    private final AmqpProducer amqpProducer;
    private final AmqpConsumer amqpConsumer;
    private final Map<String, MessageModel> messageCache;

    public GraphQLController(MessageService messageService, 
                             AmqpProducer amqpProducer, 
                             AmqpConsumer amqpConsumer) {
        this.messageService = messageService;
        this.amqpProducer = amqpProducer;
        this.amqpConsumer = amqpConsumer;
        this.messageCache = new ConcurrentHashMap<>();
    }

    @QueryMapping
    public MessagePayload message(@Argument String id) {
        log.debug("Consultando mensaje con ID: {}", id);
        MessageModel cached = messageCache.get(id);
        if (cached != null) {
            return toPayload(cached);
        }
        return null;
    }

    @QueryMapping
    public List<MessagePayload> allMessages() {
        log.debug("Consultando todos los mensajes");
        return new ArrayList<>(messageCache.values()).stream()
                .map(this::toPayload)
                .collect(Collectors.toList());
    }

    @QueryMapping
    public MessageStats stats() {
        log.debug("Consultando estadísticas de mensajes");
        long total = messageCache.size();
        long sent = messageCache.values().stream()
                .filter(m -> m.status() == MessageStatus.SENT)
                .count();
        long processed = messageCache.values().stream()
                .filter(m -> m.status() == MessageStatus.PROCESSED)
                .count();
        long failed = messageCache.values().stream()
                .filter(m -> m.status() == MessageStatus.FAILED)
                .count();
        long retry = messageCache.values().stream()
                .filter(m -> m.status() == MessageStatus.RETRY)
                .count();
        
        return new MessageStats(total, sent, processed, failed, retry);
    }

    @QueryMapping
    public Mono<ProcessingResult> processMessage(@Argument MessageInput input) {
        log.info("Procesando mensaje vía GraphQL. Payload: {}", input.payload());
        
        MessageModel message = new MessageModel(
                UUID.randomUUID().toString(),
                input.payload(),
                MessageStatus.PENDING,
                Instant.now().toString(),
                input.targetService() != null ? input.targetService() : "default-service",
                0,
                null
        );
        
        return messageService.processMessage(message)
                .doOnSuccess(result -> {
                    messageCache.put(result.correlationId(), result);
                    log.info("Mensaje procesado exitosamente. ID: {}", result.correlationId());
                })
                .map(this::toProcessingResult)
                .onErrorResume(error -> {
                    log.error("Error procesando mensaje: {}", error.getMessage());
                    return Mono.just(new ProcessingResult(
                            false,
                            error.getMessage(),
                            null,
                            0
                    ));
                });
    }

    @MutationMapping
    public Mono<MessagePayload> sendMessage(@Argument MessageInput input) {
        log.info("Enviando mensaje vía GraphQL. Payload: {}", input.payload());
        
        MessageModel message = new MessageModel(
                UUID.randomUUID().toString(),
                input.payload(),
                MessageStatus.CREATED,
                Instant.now().toString(),
                input.targetService() != null ? input.targetService() : "default-service",
                0,
                null
        );
        
        return amqpProducer.sendMessage(message)
                .doOnSuccess(result -> {
                    messageCache.put(result.correlationId(), result);
                    log.info("Mensaje enviado exitosamente. ID: {}", result.correlationId());
                })
                .map(this::toPayload)
                .onErrorResume(error -> {
                    log.error("Error enviando mensaje: {}", error.getMessage());
                    MessageModel errorMessage = message.withStatus(MessageStatus.FAILED);
                    messageCache.put(errorMessage.correlationId(), errorMessage);
                    return Mono.just(toPayload(errorMessage));
                });
    }

    @MutationMapping
    public Mono<ProcessingResult> reprocessMessage(@Argument String id) {
        log.info("Reprocesando mensaje. ID: {}", id);
        
        MessageModel cached = messageCache.get(id);
        if (cached == null) {
            return Mono.just(new ProcessingResult(false, "Mensaje no encontrado", null, 0));
        }
        
        MessageModel retryMessage = cached.withStatus(MessageStatus.PENDING)
                .withIncrementedRetryCount();
        
        return messageService.processMessage(retryMessage)
                .doOnSuccess(result -> {
                    messageCache.put(result.correlationId(), result);
                    log.info("Mensaje reprocesado. ID: {}", result.correlationId());
                })
                .map(this::toProcessingResult)
                .onErrorResume(error -> {
                    log.error("Error reprocesando mensaje: {}", error.getMessage());
                    return Mono.just(new ProcessingResult(false, error.getMessage(), null, 0));
                });
    }

    @MutationMapping
    public Mono<Boolean> deleteMessage(@Argument String id) {
        log.info("Eliminando mensaje. ID: {}", id);
        MessageModel removed = messageCache.remove(id);
        return Mono.just(removed != null);
    }

    @QueryMapping
    public Mono<QueueStats> queueStats() {
        return Mono.zip(
                amqpProducer.getMessageCount(),
                amqpConsumer.getProcessedCount(),
                amqpConsumer.getDlqCount()
        ).map(tuple -> new QueueStats(
                tuple.getT1(),
                tuple.getT2(),
                tuple.getT3(),
                tuple.getT1() - tuple.getT2()
        ));
    }

    @SchemaMapping(typeName = "MessagePayload")
    public String status(MessagePayload payload) {
        return payload.status().name();
    }

    private MessagePayload toPayload(MessageModel model) {
        return new MessagePayload(
                model.correlationId(),
                model.payload(),
                model.status(),
                model.timestamp(),
                model.targetService(),
                model.retryCount()
        );
    }

    private ProcessingResult toProcessingResult(MessageModel model) {
        return new ProcessingResult(
                model.status() == MessageStatus.PROCESSED,
                "Mensaje procesado",
                model.correlationId(),
                model.retryCount()
        );
    }

    public record MessagePayload(
            String id,
            String payload,
            MessageStatus status,
            String timestamp,
            String targetService,
            Integer retryCount
    ) {}

    public record MessageInput(
            String payload,
            String targetService
    ) {}

    public record MessageStats(
            long total,
            long sent,
            long processed,
            long failed,
            long retry
    ) {}

    public record ProcessingResult(
            boolean success,
            String message,
            String messageId,
            int retryCount
    ) {}

    public record QueueStats(
            long queued,
            long processed,
            long dlq,
            long pending
    ) {}
}

// === ARCHIVO: src/main/resources/graphql/schema.graphqls ===
scalar DateTime

type Query {
    messages(status: MessageStatus, limit: Int): [Message!]!
    messageById(id: ID!): Message
    messageStats: MessageStats!
    healthCheck: HealthStatus!
}

type Mutation {
    sendMessage(input: MessageInput!): MessageResult!
    processMessageStream(messages: [MessageInput!]!): StreamResult!
    retryMessage(id: ID!): MessageResult!
    acknowledgeMessage(id: ID!): MessageResult!
}

type Subscription {
    onMessageReceived: Message!
    onMessageProcessed: Message!
    onMessageError: MessageError!
}

type Message {
    id: ID!
    payload: String!
    status: MessageStatus!
    sourceService: String!
    targetService: String
    retryCount: Int!
    createdAt: DateTime!
    processedAt: DateTime
    errorDetails: String
    metadata: MessageMetadata
}

type MessageMetadata {
    correlationId: String!
    traceId: String
    spanId: String
    protocol: String!
    contentType: String
    priority: Int
}

type MessageStats {
    totalMessages: Int!
    pendingMessages: Int!
    processedMessages: Int!
    failedMessages: Int!
    inRetryMessages: Int!
    averageProcessingTimeMs: Float
}

type HealthStatus {
    status: String!
    services: [ServiceHealth!]!
    timestamp: DateTime!
}

type ServiceHealth {
    serviceName: String!
    isHealthy: Boolean!
    latencyMs: Int
    lastCheck: DateTime!
}

type MessageResult {
    success: Boolean!
    message: Message
    error: String
}

type StreamResult {
    totalProcessed: Int!
    successful: Int!
    failed: Int!
    results: [MessageResult!]!
}

type MessageError {
    messageId: ID!
    errorCode: String!
    errorMessage: String!
    timestamp: DateTime!
    serviceName: String!
}

enum MessageStatus {
    PENDING
    PROCESSING
    PROCESSED
    FAILED
    IN_RETRY
    DLQ
}

input MessageInput {
    payload: String!
    sourceService: String!
    targetService: String
    correlationId: String
    protocol: String = "HTTP"
    contentType: String = "application/json"
    priority: Int = 5
}

directive @auth(requires: Role) on FIELD_DEFINITION
directive @rateLimit(max: Int, window: String) on FIELD_DEFINITION

enum Role {
    ADMIN
    USER
    SERVICE
}

// === ARCHIVO: src/main/java/com/integracion/protocolos/config/RSocketConfig.java ===
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

// === ARCHIVO: src/main/java/com/integracion/protocolos/config/GrpcConfig.java ===
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

// === ARCHIVO: src/main/java/com/integracion/protocolos/config/AmqpConfig.java ===
package com.integracion.protocolos.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AmqpConfig {

    public static final String EXCHANGE_PRINCIPAL = "exchange.principal";
    public static final String EXCHANGE_DLQ = "exchange.dlq";
    public static final String QUEUE_PROCESAMIENTO = "cola.procesamiento";
    public static final String QUEUE_DLQ = "cola.dlq";
    public static final String ROUTING_KEY_PRINCIPAL = "routing.mensaje.#";
    public static final String ROUTING_KEY_DLQ = "routing.dlq";
    public static final String BINDING_PATTERN = "*.mensaje.*";

    @Bean
    public TopicExchange exchangePrincipal() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "topic");
        return ExchangeBuilder.topicExchange(EXCHANGE_PRINCIPAL)
                .durable(true)
                .autoDelete(false)
                .withArguments(arguments)
                .build();
    }

    @Bean
    public DirectExchange exchangeDlq() {
        return ExchangeBuilder.directExchange(EXCHANGE_DLQ)
                .durable(true)
                .autoDelete(false)
                .build();
    }

    @Bean
    public Queue colaProcesamiento() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", EXCHANGE_DLQ);
        arguments.put("x-dead-letter-routing-key", ROUTING_KEY_DLQ);
        arguments.put("x-message-ttl", 300000);
        arguments.put("x-max-length", 10000);
        arguments.put("x-overflow", "reject-publish");
        
        return QueueBuilder.durable(QUEUE_PROCESAMIENTO)
                .withArguments(arguments)
                .build();
    }

    @Bean
    public Queue colaDlq() {
        return QueueBuilder.durable(QUEUE_DLQ)
                .withArgument("x-message-ttl", 604800000)
                .build();
    }

    @Bean
    public Binding bindingProcesamiento(Queue colaProcesamiento, TopicExchange exchangePrincipal) {
        return BindingBuilder.bind(colaProcesamiento)
                .to(exchangePrincipal)
                .with(ROUTING_KEY_PRINCIPAL);
    }

    @Bean
    public Binding bindingDlq(Queue colaDlq, DirectExchange exchangeDlq) {
        return BindingBuilder.bind(colaDlq)
                .to(exchangeDlq)
                .with(ROUTING_KEY_DLQ);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setDefaultCharset("UTF-8");
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        template.setExchange(EXCHANGE_PRINCIPAL);
        template.setRoutingKey("mensaje.procesar");
        template.setRetryTemplate(retryTemplate());
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                System.err.println("Mensaje no confirmado: " + cause);
            }
        });
        template.setReturnsCallback(returned -> {
            System.err.println("Mensaje devuelto: " + returned.getMessage());
        });
        return template;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000);
        backOffPolicy.setMultiplier(2.0);
        backOffPolicy.setMaxInterval(10000);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setRetryableExceptions(new HashSet<>() {{
            add(Exception.class);
        }});
        retryTemplate.setRetryPolicy(retryPolicy);
        
        return retryTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, 
            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setPrefetchCount(25);
        factory.setDefaultRequeueRejected(false);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }
}

// === ARCHIVO: src/main/java/com/integracion/protocolos/config/GraphQLConfig.java ===
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

// === ARCHIVO: src/test/java/com/integracion/protocolos/api/RSocketControllerTest.java ===
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
```
