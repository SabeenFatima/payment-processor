# Design: outbox-entity

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

<!-- Background and current state -->

The Payment entity is already defined. Now we need the OutboxMessage
entity to support the Transactional Outbox Pattern. When a payment is
received, both the Payment and an OutboxMessage are saved in one single
database transaction. A background scheduler then reads unsent outbox
messages and publishes them to RabbitMQ. This guarantees no message is
lost even if the app crashes.

## Goals / Non-Goals

**Goals:**
- Define the OutboxMessage table with all required fields
- Provide a repository method to find unsent messages
- Support the outbox pattern for reliable message publishing

**Non-Goals:**
- No publishing logic in this change that belongs to OutboxScheduler
- No RabbitMQ configuration in this change
- No changes to the Payment entity

## Decisions

<!-- Key design decisions and rationale -->

**Boolean "sent" flag over deleting records**
Outbox messages are marked as sent=true instead of being deleted
after publishing. This keeps a history of all published messages
which is useful for debugging and auditing. Alternatives considered:
deleting after publish — rejected because it removes audit trail.

**Separate table over adding fields to Payment**
OutboxMessage is its own entity and table rather than adding a
"published" column to the payments table. This keeps concerns
separated — Payment tracks business state, OutboxMessage tracks
infrastructure messaging state.

**UUID primary key**
Same reasoning as Payment entity — UUID allows multiple app instances
to create outbox records without ID conflicts, which is important for
horizontal scaling.

## Risks / Trade-offs

<!-- Known risks and trade-offs -->


---

Dependencies:

### proposal — Initial proposal document outlining the change

```
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

```


Save your response to: /Users/sabeenfatima/IdeaProjects/payment-processor/openspec/changes/outbox-entity/design.md