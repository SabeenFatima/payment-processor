## ADDED Requirements

### Requirement: Payment entity persistence
The system SHALL persist every payment request to the
PostgreSQL database with a unique UUID, amount, currency,
description, status and timestamps.

#### Scenario: New payment is saved
- **WHEN** a payment is created
- **THEN** it SHALL be saved with status RECEIVED
- **THEN** it SHALL have a non-null UUID as its ID
- **THEN** it SHALL have createdAt and updatedAt timestamps

### Requirement: Payment status lifecycle
The system SHALL track payment status explicitly through
four states: RECEIVED, IN_PROGRESS, COMPLETED, FAILED.

#### Scenario: Status stored as string
- **WHEN** a payment status is saved to the database
- **THEN** it SHALL be stored as a string value not a number
- **THEN** the value SHALL be one of: RECEIVED, IN_PROGRESS,
  COMPLETED, FAILED

### Requirement: Payment data retrieval
The system SHALL allow retrieval of payments by ID and
by status.

#### Scenario: Find payment by ID
- **WHEN** a valid payment ID is provided
- **THEN** the system SHALL return the matching payment

#### Scenario: Find payments by status
- **WHEN** a PaymentStatus is provided
- **THEN** the system SHALL return all payments with that status