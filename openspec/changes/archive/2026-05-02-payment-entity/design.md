# Change: payment-entity

Create the design document that explains HOW to implement the change.

When to include design.md (create only if any apply):
- Cross-cutting change (multiple services/modules) or new architectural pattern
- New external dependency or significant data model changes
- Security, performance, or migration complexity
- Ambiguity that benefits from technical decisions before coding

Sections:
- **Context**: Background, current state, constraints, stakeholders
- **Goals / Non-Goals**: What this design achieves and explicitly excludes
- **Decisions**: Key technical choices with rationale (why X over Y?). Include alternatives considered for each decision.
- **Risks / Trade-offs**: Known limitations, things that could go wrong. Format: [Risk] → Mitigation
- **Migration Plan**: Steps to deploy, rollback strategy (if applicable)
- **Open Questions**: Outstanding decisions or unknowns to resolve

Focus on architecture and approach, not line-by-line implementation.
Reference the proposal for motivation and specs for requirements.

Good design docs explain the "why" behind technical decisions.


---

Template:
## Context

This is a project with no existing database schema.
The payment entity is the core data model that all other
components depend on. PostgreSQL is used as the database
via Spring Data JPA.

## Goals / Non-Goals

**Goals:**
- Define the Payment table with all required fields
- Define payment lifecycle states explicitly via enum
- Provide a repository interface for database access

**Non-Goals:**
- No business logic in entities
- No API endpoints in this change
- No RabbitMQ integration yet

## Decisions

**UUID over auto-increment ID**
UUIDs are used as primary keys instead of auto-increment integers.
This is safer for horizontal scaling where multiple app instances
can generate IDs without coordinating with each other.

**EnumType.STRING over EnumType.ORDINAL**
PaymentStatus is stored as text (RECEIVED, COMPLETED etc) not
as a number (0, 1, 2). This makes the database human-readable
and prevents bugs if enum order ever changes.

**@PrePersist and @PreUpdate for timestamps**
Timestamps are handled automatically by JPA lifecycle hooks
rather than manually in service code, reducing the chance
of forgetting to set them.

## Risks / Trade-offs

<!-- Known risks and trade-offs -->

---

Dependencies:

### proposal — Initial proposal document outlining the change

```
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

```


Save your response to: /Users/sabeenfatima/IdeaProjects/payment-processor/openspec/changes/payment-entity/design.md