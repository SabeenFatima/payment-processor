## Why

The application needs to receive payment requests from 
clients via REST API. This change exposes the two required 
endpoints — one to submit a payment and one to check its 
status. DTOs are used to keep the API contract separate 
from the database entities.

## What Changes

- PaymentRequest.java DTO with validation annotations
- PaymentResponse.java DTO for API responses
- PaymentService.java with receivePayment() and getPayment()
- PaymentController.java with POST /payments and GET /payments/{id}

## Capabilities

### New Capabilities
<!-- Capabilities being introduced. Use kebab-case identifiers (e.g., user-auth, data-export). Each creates specs/<name>/spec.md -->

### Modified Capabilities
<!-- Existing capabilities whose REQUIREMENTS are changing. Use existing spec names from openspec/specs/. -->

## Impact

<!-- Affected code, APIs, dependencies, systems -->
