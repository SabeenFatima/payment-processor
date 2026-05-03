## Why

Ensure reliable message publishing to RabbitMQ without data loss by avoiding direct publish from controller.

## What Changes

- Add OutboxScheduler to poll outbox_messages table periodically
- Publish unsent messages to RabbitMQ exchange
- Mark messages as sent after successful publish
- Retry failed messages in next cycle
- Enable scheduling in PaymentApplication

## Capabilities

### New Capabilities
<!-- Capabilities being introduced. Use kebab-case identifiers (e.g., user-auth, data-export). Each creates specs/<name>/spec.md -->

### Modified Capabilities
<!-- Existing capabilities whose REQUIREMENTS are changing. Use existing spec names from openspec/specs/. -->

## Impact

<!-- Affected code, APIs, dependencies, systems -->
