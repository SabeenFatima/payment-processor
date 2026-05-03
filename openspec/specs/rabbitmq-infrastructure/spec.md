# rabbitmq-infrastructure Specification

## Purpose
TBD - created by archiving change rabbitmq-config. Update Purpose after archive.
## Requirements
### Requirement: Main payment queue declaration
The system SHALL declare a durable main queue for processing
payments. The queue SHALL be linked to a dead letter exchange
so failed messages are automatically routed there.

#### Scenario: Queue exists on startup
- **WHEN** the application starts
- **THEN** a durable queue named payments.queue SHALL exist in RabbitMQ
- **THEN** the queue SHALL be visible in the RabbitMQ management UI at localhost:15672

#### Scenario: Queue survives RabbitMQ restart
- **WHEN** RabbitMQ is restarted
- **THEN** the payments.queue SHALL still exist with all pending messages intact

### Requirement: Dead letter queue declaration
The system SHALL declare a dead letter queue to capture
payments that fail processing repeatedly. No payment SHALL ever be silently dropped.

#### Scenario: Dead letter queue exists on startup
- **WHEN** the application starts
- **THEN** a durable queue named payments.dead.letter.queue SHALL exist in RabbitMQ
- **THEN** failed messages SHALL be automatically routed to this queue

### Requirement: Exchange and binding configuration
The system SHALL declare a direct exchange and bind it to 
the main payment queue using a routing key. All messages
SHALL be published to the exchange, not directly to the queue.

#### Scenario: Exchange routes message to queue
- **WHEN** a message is published to payments.exchange with routing key payments.routing.key
- **THEN** the message SHALL be delivered to payments.queue

### Requirement: JSON message conversion
The system SHALL convert all RabbitMQ messages to and from
JSON format using Jackson. Messages SHALL be human readable
in the RabbitMQ management UI.

#### Scenario: Message is readable in management UI
- **WHEN** a payment message is published to RabbitMQ
- **THEN** the message body SHALL be valid JSON
- **THEN** the payment id SHALL be readable as plain text in the UI

### Requirement: RestTemplate bean for HTTP calls
The system SHALL provide a RestTemplate bean for making
HTTP calls to the Mock Payment Service from the PaymentWorker.

#### Scenario: RestTemplate is available for injection
- **WHEN** PaymentWorker is initialized by Spring
- **THEN** RestTemplate SHALL be injected successfully with no errors

