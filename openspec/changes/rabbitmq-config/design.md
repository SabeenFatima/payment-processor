# Design: rabbitmq-config

## Context
The REST API and database layers are working. Now we need to configure
RabbitMQ so the outbox scheduler and payment worker can communicate
through a message queue. This change only sets up the configuration.
All other components depend on these queue declarations existing before
they can function.

## Goals / Non-Goals

**Goals:**
- Declare the main payment queue where messages are published
- Declare the exchange and binding that routes messages to the queue
- Declare a dead letter queue for repeatedly failed payments
- Configure JSON message conversion for RabbitMQ messages
- Configure RestTemplate bean for calling the mock Payment Service

**Non-Goals:**
- No message publishing logic in this change (that is OutboxScheduler)
- No message consuming logic in this change (that is PaymentWorker)
- No retry logic configuration in this change

## Decisions

**Dead Letter Queue over silently dropping failed messages**
When a message fails repeatedly, RabbitMQ automatically routes it
to the dead letter queue instead of dropping it. This means no
payment is ever silently lost and failed messages can be inspected
and replayed manually. 

**Jackson2JsonMessageConverter over default Java serialization**
Messages are converted to JSON instead of using Java's built-in
serialization. JSON is human readable, easier to debug in the
RabbitMQ management UI, and not tied to Java so other services
could consume the queue in future. Alternatives considered:
default Java serialization was rejected because it produces binary
unreadable messages and breaks if class names change.

**Durable queues**
Both main and dead letter queues are declared as durable. This means
RabbitMQ persists them to disk. If RabbitMQ restarts, the queues
and their messages survive. Non-durable queues would lose all
pending messages on RabbitMQ restart.

## Risks / Trade-offs

[Risk] Queue already exists in RabbitMQ with different configuration
- Mitigation: RabbitMQ will throw a channel error if queue properties
conflict. Solution is to delete the queue from the management UI
at localhost:15672 and restart the app.

[Risk] Dead letter queue grows large if many payments fail
- Mitigation: Acceptable for this assignment scope. In production
a monitoring alert would be set up on dead letter queue size.