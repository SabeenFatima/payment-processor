# payment-processing Specification

## Purpose
TBD - created by archiving change payment-worker. Update Purpose after archive.
## Requirements
### Requirement: Payment message consumption
The system SHALL consume payment messages from payments.queue
and process each payment by calling the Mock Payment Service.
Each message SHALL be acknowledged manually only after the
final payment status is saved to the database.

#### Scenario: Message consumed and processed successfully
- **WHEN** a payment id message arrives in payments.queue
- **THEN** worker SHALL update payment status to IN_PROGRESS
- **THEN** worker SHALL call Mock Payment Service with the payment id
- **THEN** worker SHALL update payment status to COMPLETED on success
- **THEN** worker SHALL send ACK to RabbitMQ after saving to DB

#### Scenario: Mock Payment Service returns failure
- **WHEN** Mock Payment Service returns a 500 error
- **THEN** worker SHALL update payment status to FAILED
- **THEN** worker SHALL still send ACK to RabbitMQ
- **THEN** payment SHALL remain in DB with status FAILED

#### Scenario: Worker crashes before sending ACK
- **WHEN** worker crashes after calling Mock Service but before ACK
- **THEN** RabbitMQ SHALL redeliver the message to another worker
- **THEN** worker SHALL check current status before processing
- **THEN** if already COMPLETED or FAILED worker SHALL skip and ACK

### Requirement: Payment status idempotency
The system SHALL handle duplicate message delivery safely.
If a payment is already COMPLETED or FAILED the worker SHALL
skip processing and acknowledge the message without changes.

#### Scenario: Duplicate message received
- **WHEN** same payment id is delivered twice to the worker
- **THEN** worker SHALL check current payment status first
- **THEN** if status is COMPLETED worker SHALL skip and ACK
- **THEN** if status is FAILED worker SHALL skip and ACK
- **THEN** payment status SHALL NOT be changed by duplicate message

### Requirement: Mock Payment Service simulation
The system SHALL provide a Mock Payment Service endpoint that
simulates a real external payment API with random delays and
occasional failures.

#### Scenario: Successful payment processing with delay
- **WHEN** Mock Payment Service receives a process request
- **THEN** it SHALL wait between 10ms and 2000ms randomly
- **THEN** it SHALL return 200 OK with body SUCCESS 90% of the time

#### Scenario: Simulated payment failure
- **WHEN** Mock Payment Service randomly selects failure case
- **THEN** it SHALL return 500 Internal Server Error
- **THEN** it SHALL return body FAILED
- **THEN** this SHALL happen approximately 10% of the time

