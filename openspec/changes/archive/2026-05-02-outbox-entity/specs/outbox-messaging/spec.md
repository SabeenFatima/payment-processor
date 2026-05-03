## ADDED Requirements

### Requirement: Outbox message persistence
The system SHALL persist an OutboxMessage record to the database
in the same transaction as the Payment entity. If either save
fails, both SHALL be rolled back.

#### Scenario: Outbox message saved with payment
- **WHEN** a new payment is received
- **THEN** an OutboxMessage SHALL be created with sent=false
- **THEN** the OutboxMessage SHALL reference the Payment by paymentId
- **THEN** both records SHALL exist in the database together

#### Scenario: Transaction rollback on failure
- **WHEN** saving the payment or outbox message fails
- **THEN** neither record SHALL be persisted to the database

### Requirement: Outbox message status tracking
The system SHALL track whether each outbox message has been
published to RabbitMQ using a boolean sent flag.

#### Scenario: Message starts as unsent
- **WHEN** an OutboxMessage is first created
- **THEN** its sent field SHALL be false by default

#### Scenario: Unsent messages are queryable
- **WHEN** the scheduler looks for messages to publish
- **THEN** the system SHALL return only messages where sent=false

### Requirement: Outbox message retrieval by status
The system SHALL provide a repository method to find all
unsent outbox messages for the scheduler to process.

#### Scenario: Find unsent messages
- **WHEN** findBySentFalse() is called
- **THEN** the system SHALL return all OutboxMessage records
  where sent=false
- **THEN** already published messages SHALL NOT be returned