## Why

Process payments from RabbitMQ reliably and update their status using an external service, ensuring no loss via retries.

## What Changes

- Add PaymentWorker to consume messages and process payments
- Update status: IN_PROGRESS -> COMPLETED / FAILED
- Integrate MockPaymentService with delay and failure simulation
- Use manual acknowledgement for retry on failure

## Capabilities

### New Capabilities
<!-- Capabilities being introduced. Use kebab-case identifiers (e.g., user-auth, data-export). Each creates specs/<name>/spec.md -->

### Modified Capabilities
<!-- Existing capabilities whose REQUIREMENTS are changing. Use existing spec names from openspec/specs/. -->

## Impact

<!-- Affected code, APIs, dependencies, systems -->
