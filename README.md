# Payment Processing Application

Implementing a reliable async payment processing system 
using Java, Spring Boot, PostgreSQL and RabbitMQ.

## What It Does
The application receives payment requests via a REST API. It is 
saved to the database and queued for async processing. The worker 
picks it up, calls the Payment Service, and updates the status. The 
client can check the final status at any time using GET /payments/{id}.
It is designed to never lose a payment even if the application 
crashes mid-processing and restarts.

## Architecture and Design Decisions

### Components

- **REST API**: receives payment requests and returns status immediately
- **PostgreSQL**: stores all payments and outbox messages
- **RabbitMQ**: message queue for reliable async processing
- **Outbox Scheduler**: polls DB every second and publishes unsent messages to queue
- **Payment Worker**: consumes queue messages and calls the Payment Service
- **Mock Payment Service**: simulates an external payment API with random 10ms to 2s delay
- **Startup Recovery**: detects and re-queues stuck payments on restart

### Payment States
- **RECEIVED**: initial state when payment is created
- **IN_PROGRESS**: when worker starts processing
- **COMPLETED**: when payment service call succeeds
- **FAILED**: when payment service call fails

### Why RabbitMQ was chosen
RabbitMQ was chosen because it is simpler to set up and sufficient
for this scale. It provides reliable message delivery and supports 
the outbox pattern well.

### Why Transactional Outbox Pattern

When a payment is received, both the payment record and an outbox
message are saved to the database in one transaction. A background
scheduler then reads unsent outbox messages every second and
publishes them to RabbitMQ. This means if the application crashes 
after saving but before the scheduler publishes, the outbox message 
is still in the database. When the app restarts the scheduler picks 
it up and publishes it automatically. So, no payment is ever lost.

I chose this over publishing directly to RabbitMQ because if the app crashes between saving and publishing,
the message would be permanently lost with no way to recover it.

### Why Manual RabbitMQ Acknowledgement

The worker only tells RabbitMQ a message is done after saving the
final status to the database. If the worker crashes before that point,
RabbitMQ re-queues the message automatically. Auto-acknowledgement was
not used because it would lose the message if the worker crashes
mid-processing.

### Why UUID Primary Keys

UUID is used instead of auto-increment integers so multiple app
instances can generate IDs independently without coordinating with
each other. This is important for horizontal scaling.

### Why PostgreSQL

PostgreSQL was chosen for its reliability, good JPA support and
wide use in enterprise Java. It was easy to set up with Docker.
---
### Assumptions Made

- The Mock Payment Service runs inside the same application for
  simplicity. In production, it would be a separate external service
- A 10% random failure rate is simulated in the Mock Payment Service
  to demonstrate FAILED status handling
- Payments stuck in "IN_PROGRESS" after a crash are re-queued on restart
  with no limit on retries

---
## How Reliability and Restart Behavior is Ensured

### No loss on restart
1. The outbox pattern saves the payment and outbox message in one
   single database transaction — if either fails, both roll back
2. On startup, StartupRecoveryService finds any payments stuck in
   "IN_PROGRESS" status and re-queues them through the outbox flow
3. RabbitMQ queues are declared as durable so pending messages
   survive a RabbitMQ restart

### No loss of Payment Service responses
1. The worker uses manual RabbitMQ acknowledgement
2. Payment status is saved to the database before ACK is sent
3. If the worker crashes before ACK, RabbitMQ redelivers the message
4. The worker checks current payment status before processing to
   handle duplicate messages safely (idempotency)

### Horizontal scalability
1. RabbitMQ delivers each message to exactly one worker instance
2. Multiple instances share the same PostgreSQL database as the
   single source of truth
3. UUID primary keys allow multiple instances to create records
   without ID conflicts
---

## OpenSpec Usage

I used OpenSpec to plan each feature before writing any code. For
every feature I wrote a proposal explaining why it was needed, a
design document explaining the technical decisions, a spec file
defining what the system should do, and a task list breaking down
the implementation steps.

The openspec/ folder contains the full specification:
- config.yaml — project context and development rules
- changes/ — one folder per feature with proposal, design, specs
  and tasks

This approach helped me think through design decisions like choosing
the outbox pattern and manual acknowledgements before writing any
code, and kept the development process structured and traceable. It
was very transparent to see how each requirement was implemented and 
tested, and it made it easier to explain my design choices in this README.

---

## How to Run

### Prerequisites
- Java 17 or higher
- Docker and Docker Compose
- Maven

### Step 1 — Start Infrastructure

```bash
docker-compose up -d
```


This starts PostgreSQL on port 5432 and RabbitMQ on port 5672.
The RabbitMQ management UI is available at http://localhost:15672
using guest as both username and password.

### Step 2 — Run the Application

Open the project in IntelliJ IDEA and click the play button,
or run from terminal:

```bash
mvn spring-boot:run
```

The application starts on port 8080.

### Step 3 — Test the API
I used tests.http file for API testing.

Create a payment:

```http
POST http://localhost:8080/payments
Content-Type: application/json

{
  "amount": 99.99,
  "currency": "USD",
  "description": "Test payment"
}
```

Check payment status:

```http
GET http://localhost:8080/payments/{id}
```
---

## API Reference

### POST /payments

Receives a new payment request. Returns immediately with status
RECEIVED — processing happens in the background.

**Request body:**
```json
{
  "amount": 99.99,
  "currency": "USD",
  "description": "Payment description"
}
```

**Response 202 Accepted:**
```json
{
  "id": "abc-123",
  "amount": 99.99,
  "currency": "USD",
  "description": "Payment description",
  "status": "RECEIVED",
  "createdAt": "2026-05-03T10:00:00",
  "updatedAt": "2026-05-03T10:00:00"
}
```

**Response 400 Bad Request** if amount or currency is missing.

---

### GET /payments/{id}

Returns the current status of a payment.

**Response 200 OK:**
```json
{
  "id": "abc-123",
  "amount": 99.99,
  "currency": "USD",
  "description": "Payment description",
  "status": "COMPLETED",
  "createdAt": "2026-05-03T10:00:00",
  "updatedAt": "2026-05-03T10:00:02"
}
```

Possible status values: RECEIVED, IN_PROGRESS, COMPLETED, FAILED

**Response 404 Not Found** if payment ID does not exist.

---

## How to Run Performance Tests

Performance tests were run using the Gatling Maven plugin.

1. Open Maven panel in IntelliJ (View → Tool Windows → Maven)
2. Expand Plugins → gatling → gatling:test
3. Right click gatling:test → Run Maven Build
4. When prompted enter 0 for PaymentSimulation (single instance)
   or 1 for PaymentSimulationScaled (two instances)

Gatling HTML reports are generated in target/gatling/ folder.
Open index.html in a browser to view the full report.

---

## Performance Results Summary

| Metric             | Single Instance | Two Instances |
|--------------------|-----------------|---------------|
| Throughput         | 10 req/s        | 20 req/s      |
| Mean response time | 35ms            | 17ms          |
| 95th percentile    | 60ms            | 43ms          |
| 99th percentile    | 1,100ms         | 271ms         |
| Error rate         | 0%              | 0%            |

Full results, observations and suggested optimizations are in
TEST-REPORT.md.

---

## Project Structure

- src/main/java/com/sabeenfatima/paymentprocessor/ 
  - config/
    - RabbitMQConfig.java
    - AppConfig.java 
  - controller/
    - PaymentController.java 
    - MockPaymentServiceController.java 
  - dto/ 
    - PaymentRequest.java
    - PaymentResponse.java
  - entity/ 
    - Payment.java
    - PaymentStatus.java 
    - OutboxMessage.java
  - repository/
    - PaymentRepository.java
    - OutboxMessageRepository.java
  - service/
    - PaymentService.java
    - OutboxScheduler.java
    - PaymentWorker.java
    - StartupRecoveryService.java
- src/test/java/com/sabeenfatima/paymentprocessor/ 
  - simulations/ — Gatling performance test simulations
    - PaymentSimulation.java
    - PaymentSimulationScaled.java


---

## Tech Stack

| Technology     | Version      | Purpose                  |
|----------------|--------------|--------------------------|
| Java           | 17           | Programming language     |
| Spring Boot    | 4.0.6        | Application framework    |
| PostgreSQL     | latest       | Database                 |
| RabbitMQ       | 3-management | Message queue            |
| Docker Compose | —            | Local infrastructure     |
| Gatling        | 3.15.0       | Performance testing      |
| Lombok         | —            | Reduces boilerplate code |
| Maven          | —            | Build tool               |
