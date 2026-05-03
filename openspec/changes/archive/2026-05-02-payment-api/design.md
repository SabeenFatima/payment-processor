# Design: payment-api

## Context
Payment and OutboxMessage entities are already defined. Now we need
to expose the application to the outside world via REST API. Clients
will submit payment requests and check payment status through two
endpoints. DTOs are introduced to separate the API contract from
internal database entities. So that the database
model never accidentally break the API response format.

## Goals / Non-Goals

**Goals:**
- Expose POST /payments to receive new payment requests
- Expose GET /payments/{id} to check payment status
- Validate incoming requests before saving to database
- Return DTOs in all responses, never raw JPA entities
- Save payment with RECEIVED status and outbox record in one transaction

**Non-Goals:**
- No RabbitMQ publishing in this change — that is OutboxScheduler
- No authentication or authorization
- No payment update or delete endpoints
- No pagination for listing payments

## Decisions

**202 Accepted over 201 Created for POST /payments**
POST /payments returns 202 Accepted instead of 201 Created because
the payment is not fully processed at the time of the response — it
is only received and queued. 201 Created implies the resource is
complete which would be misleading here.

**DTOs over exposing entities directly**
PaymentRequest and PaymentResponse are separate classes from the
Payment entity. This protects the API from internal changes — if
a database column is renamed, the API response stays the same.
Alternatives considered: exposing entity directly — rejected because
it tightly couples API to database schema.

**Service layer for business logic**
PaymentController only handles HTTP concerns. All business logic
like saving payment and creating outbox record lives in
PaymentService. This keeps the controller thin and the service
independently testable.

**@Valid annotation for input validation**
Bean Validation annotations on PaymentRequest ensure invalid
requests are rejected before reaching the service layer. This
prevents bad data from ever reaching the database.

## Risks / Trade-offs

[Risk] Client receives 202 but payment later fails
- Mitigation: Client must poll GET /payments/{id} to check
final status. This is documented and expected behavior.

[Risk] Service saves payment but outbox record fails
- Mitigation: Both saves happen inside @Transactional method in PaymentService. So
if either fails both roll back, keeping DB consistent.