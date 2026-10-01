# Comparación y aplicación de protocolos de comunicación avanzados

Explora y compara diferentes protocolos de comunicación no convencionales utilizados en arquitecturas de integración y microservicios. Enfócate en cómo RSocket se diferencia de HTTP/1, HTTP/2 y WebSocket, y cómo gRPC facilita la comunicación entre servicios. Analiza el manejo de mensajes y colas en AMQP, y compara el rendimiento y flexibilidad de GraphQL con REST.

## Informacion General

| Campo | Valor |
|-------|-------|
| **Tema** | Protocolos de comunicación no convencionales |
| **Nivel** | advanced-l2 |
| **Tipo** | theoretical |
| **Tiempo estimado** | 6 horas |

## Fases del Reto

### Fase 0: Configuración del Proyecto

**Objetivo:** Obtener el proyecto base funcional enviando el Código Base a un asistente de IA, que lo analizará, corregirá errores y generará un ZIP listo para usar.

**Tiempo estimado:** 15-30 minutos

**Instrucciones:**

- Asegúrate de tener instalado para ejecutar el proyecto: JDK 17+, Maven 3.9+, IDE con soporte Java.
- Copia todo el contenido del campo **Código Base** de este reto — incluyendo el texto de instrucciones que aparece al inicio.
- Abre un asistente de IA (Claude en claude.ai, ChatGPT o Gemini — se recomienda Claude), pega el contenido copiado en el chat y envíalo.
- El asistente analizará los archivos, corregirá errores y generará un archivo ZIP descargable. Descárgalo y extráelo en la carpeta donde quieras trabajar.
- Ejecuta `mvn compile` en la raíz. Si no hay errores, estás listo.

**Entregable:** El proyecto compila/arranca sin errores.

<details>
<summary>Pistas de conocimiento</summary>

- Copia el Código Base completo incluyendo el texto de instrucciones al inicio — esas instrucciones le indican al asistente exactamente qué hacer con los archivos.
- Si el asistente no genera el ZIP automáticamente al terminar el análisis, escríbele: "genera el ZIP ahora".
- Si el proyecto tiene errores al arrancar, comparte el mensaje de error con el mismo asistente para que lo corrija.

</details>

### Fase 1: Comparación de protocolos de comunicación

**Objetivo:** Entender las diferencias y ventajas de RSocket frente a HTTP/1, HTTP/2 y WebSocket.

**Tiempo estimado:** 2 horas

**Instrucciones:**

- Investiga y compara RSocket con HTTP/1, HTTP/2 y WebSocket.
- Identifica las principales diferencias en términos de rendimiento, latencia y uso de recursos.
- Prepara un resumen de las ventajas y desventajas de cada protocolo.

**Entregable:** Resumen comparativo de RSocket vs HTTP/1, HTTP/2 y WebSocket.

<details>
<summary>Pistas de conocimiento</summary>

- Considera el contexto de uso de cada protocolo en diferentes escenarios de integración.
- Piensa en cómo cada protocolo maneja la comunicación bidireccional y la latencia.

</details>

### Fase 2: Aplicación de gRPC en microservicios

**Objetivo:** Entender cómo gRPC facilita la comunicación entre servicios en una arquitectura de microservicios.

**Tiempo estimado:** 2 horas

**Instrucciones:**

- Investiga cómo gRPC mejora la comunicación entre servicios en comparación con otros protocolos.
- Identifica los beneficios de usar gRPC en términos de rendimiento y escalabilidad.
- Prepara un informe sobre las mejores prácticas para implementar gRPC en una arquitectura de microservicios.

**Entregable:** Informe sobre la aplicación de gRPC en microservicios.

<details>
<summary>Pistas de conocimiento</summary>

- Considera casos de uso específicos donde gRPC ofrece ventajas sobre otros protocolos.
- Piensa en cómo gRPC maneja la serialización y deserialización de datos.

</details>

### Fase 3: Manejo de mensajes y colas en AMQP

**Objetivo:** Entender cómo se manejan los mensajes y las colas en AMQP.

**Tiempo estimado:** 2 horas

**Instrucciones:**

- Investiga cómo AMQP maneja la comunicación asincrónica y el enrutamiento de mensajes.
- Identifica las ventajas de usar AMQP en sistemas de mensajería.
- Prepara un informe sobre las mejores prácticas para implementar AMQP en un sistema de integración.

**Entregable:** Informe sobre el manejo de mensajes y colas en AMQP.

<details>
<summary>Pistas de conocimiento</summary>

- Considera casos de uso donde AMQP ofrece ventajas sobre otros sistemas de mensajería.
- Piensa en cómo AMQP garantiza la entrega de mensajes y maneja la consistencia.

</details>

## Dimensiones Evaluadas

- **queEs**: ¿Qué es RSocket y cómo se diferencia de otros protocolos de comunicación?
- **paraQueSirve**: ¿Para qué sirve gRPC en una arquitectura de microservicios?
- **comoSeUsa**: ¿Cómo se usan las colas y el enrutamiento de mensajes en AMQP?
- **erroresComunes**: ¿Cuáles son los errores comunes al implementar protocolos de comunicación no convencionales?
- **queDecisionesImplica**: ¿Qué decisiones implica elegir un protocolo de comunicación sobre otro en un proyecto de integración?

## Criterios de Evaluacion

- Comparación detallada de RSocket con HTTP/1, HTTP/2 y WebSocket.
- Informe sobre las ventajas y desventajas de gRPC en microservicios.
- Informe sobre el manejo de mensajes y colas en AMQP.

## Como trabajar con un asistente de IA

Hay dos caminos, elegi uno:

- **AGENTS.md** (recomendado) — instrucciones nativas del repo. Abri esta carpeta con tu agente local (Claude Code, Cursor, Codex, Copilot, Gemini) y las carga solo. Sabe que archivos faltan y con que comando se verifica, y completa el scaffold escribiendo en disco.
- **PROMPT_MEJORA.md** — para copiar y pegar en un chat (claude.ai, ChatGPT). Devuelve un ZIP con el proyecto. Sirve si no tenes un agente en el IDE.

Ninguno de los dos resuelve las fases del reto: eso es tu trabajo.

## Verificacion

El proyecto esta listo para trabajar cuando este comando corre sin errores:

```bash
npx --yes @redocly/cli lint openapi.yaml
```

---

*Reto generado automaticamente por Challenge Generator - Pragma*
