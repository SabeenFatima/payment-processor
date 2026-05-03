# restart-recovery Specification

## Purpose
TBD - created by archiving change startup-recovery. Update Purpose after archive.
## Requirements
### Requirement: Automatic recovery on startup
The system SHALL automatically detect and recover all payments
stuck in IN_PROGRESS status every time the application starts.
Recovery SHALL complete before any new payments are processed.

#### Scenario: Stuck payments detected on startup
- **WHEN** application starts and payments table contains records with status IN_PROGRESS
- **THEN** StartupRecoveryService SHALL find all IN_PROGRESS payments
- **THEN** each payment SHALL be reset to RECEIVED status
- **THEN** a new OutboxMessage SHALL be created for each recovered payment
- **THEN** OutboxScheduler SHALL pick them up and republish to RabbitMQ

#### Scenario: No stuck payments on startup
- **WHEN** application starts and no payments have status IN_PROGRESS
- **THEN** StartupRecoveryService SHALL do nothing
- **THEN** application SHALL start normally with no recovery actions

### Requirement: Transactional recovery per payment
The system SHALL recover each stuck payment atomically. If
resetting a payment or creating its OutboxMessage fails,
both operations SHALL roll back and the payment SHALL remain
IN_PROGRESS to be retried on the next restart.

#### Scenario: Recovery succeeds for all stuck payments
- **WHEN** startup recovery runs with multiple IN_PROGRESS payments
- **THEN** each payment SHALL be reset to RECEIVED in its own transaction
- **THEN** each payment SHALL have a new OutboxMessage with sent=false
- **THEN** all recovered payments SHALL eventually reach COMPLETED or FAILED

#### Scenario: Recovery fails for one payment
- **WHEN** resetting one payment fails due to a database error
- **THEN** that payment SHALL remain IN_PROGRESS
- **THEN** other payments SHALL still be recovered successfully
- **THEN** failed payment SHALL be retried on next application restart

### Requirement: No duplicate processing of recovered payments
The system SHALL not process a recovered payment more than once.
If a recovered payment is already COMPLETED or FAILED by the time
the worker receives its re-queued message, the worker SHALL skip it.

#### Scenario: Recovered payment already completed
- **WHEN** a recovered payment reaches the worker via RabbitMQ
- **THEN** worker SHALL check current status before processing
- **THEN** if status is COMPLETED or FAILED worker SHALL skip and ACK
- **THEN** payment status SHALL NOT be changed

