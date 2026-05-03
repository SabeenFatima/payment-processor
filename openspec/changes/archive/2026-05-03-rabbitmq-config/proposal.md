## Why

Before the outbox scheduler or payment worker can use 
RabbitMQ, the queues, exchange and bindings must be declared. A dead letter queue is also configured to handle 
payments that repeatedly fail processing so they are never silently lost

## What Changes

- RabbitMQConfig.java with queue, exchange and binding declarations
- Dead letter queue and dead letter exchange configuration
- Jackson2JsonMessageConverter bean for JSON messaging
- RabbitTemplate bean configuration
- AppConfig.java with RestTemplate bean for calling Payment Service

## Capabilities

### New Capabilities
<!-- Capabilities being introduced. Use kebab-case identifiers (e.g., user-auth, data-export). Each creates specs/<name>/spec.md -->

### Modified Capabilities
<!-- Existing capabilities whose REQUIREMENTS are changing. Use existing spec names from openspec/specs/. -->

## Impact

<!-- Affected code, APIs, dependencies, systems -->
