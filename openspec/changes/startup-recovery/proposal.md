## Why

If the app crashes, payments stuck in IN_PROGRESS never complete. On restart, this recovers them to prevent loss.

## What Changes

- Add StartupRecoveryService (runs on ApplicationReadyEvent)
- Find all IN_PROGRESS payments
- Reset to RECEIVED
- Create new OutboxMessage so they are reprocessed

## Capabilities

### New Capabilities
<!-- Capabilities being introduced. Use kebab-case identifiers (e.g., user-auth, data-export). Each creates specs/<name>/spec.md -->

### Modified Capabilities
<!-- Existing capabilities whose REQUIREMENTS are changing. Use existing spec names from openspec/specs/. -->

## Impact

<!-- Affected code, APIs, dependencies, systems -->
