# outbox-scheduling Specification

## Purpose
TBD - created by archiving change outbox-pattern. Update Purpose after archive.
## Requirements
### Requirement: Outbox scheduler polling
The system SHALL poll the outbox_messages table every second
and publish all unsent messages to RabbitMQ. The scheduler
SHALL run automatically on application startup.

#### Scenario: Unsent messages are published
- **WHEN** outbox_messages table contains records with sent=false
- **THEN** scheduler SHALL publish each payment id to payments.exchange
- **THEN** each published message SHALL be marked as sent=true in DB
- **THEN** messages SHALL no longer appear in findBySentFalse() results

#### Scenario: No unsent messages
- **WHEN** outbox_messages table has no records with sent=false
- **THEN** scheduler SHALL do nothing and wait for next cycle

### Requirement: Failed publish retry
The system SHALL retry publishing a message on the next
scheduler cycle if publishing to RabbitMQ fails. No message
SHALL be permanently lost due to a single publish failure.

#### Scenario: Publish fails
- **WHEN** RabbitMQ is temporarily unavailable during publish
- **THEN** message SHALL remain as sent=false in the database
- **THEN** scheduler SHALL attempt to publish it again on next cycle

#### Scenario: Publish succeeds after retry
- **WHEN** RabbitMQ becomes available again
- **THEN** scheduler SHALL successfully publish the message
- **THEN** message SHALL be marked as sent=true

### Requirement: Restart recovery via outbox
The system SHALL automatically recover unsent messages after
a restart. Any payment saved before a crash SHALL be published
to RabbitMQ after the application restarts.

#### Scenario: App crashes after saving payment
- **WHEN** app crashes after saving payment and outbox record
- **THEN** outbox record SHALL still exist in DB with sent=false
- **THEN** after restart scheduler SHALL pick it up and publish it
- **THEN** payment SHALL continue processing normally

