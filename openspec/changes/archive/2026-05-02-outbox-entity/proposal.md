## Why

The outbox pattern requires its own database table to store 
messages that need to be published to RabbitMQ. Saving the outbox 
record in the same transaction as the Payment guarantees no message 
is ever lost if the app crashes between saving and publishing.

## What Changes

- OutboxMessage.java JPA entity with id, paymentId, sent, createdAt
- OutboxMessageRepository.java interface extending JpaRepository

## Capabilities

### New Capabilities
<!-- Capabilities being introduced. Use kebab-case identifiers (e.g., user-auth, data-export). Each creates specs/<name>/spec.md -->

### Modified Capabilities
<!-- Existing capabilities whose REQUIREMENTS are changing. Use existing spec names from openspec/specs/. -->

## Impact

<!-- Affected code, APIs, dependencies, systems -->
