## Why

Database should be defined before going further into backend.

## What Changes

- PaymentStatus.java enum (RECEIVED, IN_PROGRESS, COMPLETED, FAILED)
- Payment.java JPA entity with id, amount, currency, status, timestamps
- PaymentRepository.java interface extending JpaRepository

## Capabilities

### New Capabilities
<!-- Capabilities being introduced. Use kebab-case identifiers (e.g., user-auth, data-export). Each creates specs/<name>/spec.md -->

### Modified Capabilities
<!-- Existing capabilities whose REQUIREMENTS are changing. Use existing spec names from openspec/specs/. -->

## Impact

<!-- Affected code, APIs, dependencies, systems -->
