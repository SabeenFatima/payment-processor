# payment-api Specification

## Purpose
TBD - created by archiving change payment-api. Update Purpose after archive.
## Requirements
### Requirement: Payment submission
The system SHALL accept payment requests via POST /payments
and return 202 Accepted immediately after saving to database.

#### Scenario: Valid payment submitted
- **WHEN** client sends POST /payments with valid amount, currency and description
- **THEN** system SHALL save payment with status RECEIVED
- **THEN** system SHALL return 202 Accepted with payment id and status

#### Scenario: Invalid payment rejected
- **WHEN** client sends POST /payments with missing or invalid fields
- **THEN** system SHALL return 400 Bad Request
- **THEN** no payment SHALL be saved to database

### Requirement: Payment status check
The system SHALL allow clients to check payment status via GET /payments/{id}.

#### Scenario: Valid payment id provided
- **WHEN** client sends GET /payments/{id} with existing payment id
- **THEN** system SHALL return 200 OK with current payment status

#### Scenario: Invalid payment id provided
- **WHEN** client sends GET /payments/{id} with non-existing id
- **THEN** system SHALL return 404 Not Found

### Requirement: DTO based responses
The system SHALL never expose JPA entities directly in API responses.
All responses SHALL use PaymentResponse DTO.

#### Scenario: Response contains only DTO fields
- **WHEN** any payment endpoint returns a response
- **THEN** response SHALL contain id, amount, currency, description, status, createdAt, updatedAt
- **THEN** response SHALL NOT contain any JPA or Hibernate internal fields

