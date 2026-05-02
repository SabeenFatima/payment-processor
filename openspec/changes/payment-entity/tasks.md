# Change: payment-entity

Create the task list that breaks down the implementation work.

**IMPORTANT: Follow the template below exactly.** The apply phase parses
checkbox format to track progress. Tasks not using `- [ ]` won't be tracked.

Guidelines:
- Group related tasks under ## numbered headings
- Each task MUST be a checkbox: `- [ ] X.Y Task description`
- Tasks should be small enough to complete in one session
- Order tasks by dependency (what must be done first?)

Example:
```
## 1. Setup

- [ ] 1.1 Create new module structure
- [ ] 1.2 Add dependencies to package.json

## 2. Core Implementation

- [ ] 2.1 Implement data export function
- [ ] 2.2 Add CSV formatting utilities
```

Reference specs for what needs to be built, design for how to build it.
Each task should be verifiable - you know when it's done.


---

Template:

## 1. Enum

- [X] 1.1 Create PaymentStatus.java enum with four values:
  RECEIVED, IN_PROGRESS, COMPLETED, FAILED

## 2. Payment Entity

- [x] 2.1 Create Payment.java with @Entity and @Table annotations
- [x] 2.2 Add UUID primary key with @GeneratedValue
- [x] 2.3 Add amount, currency, description fields
- [x] 2.4 Add status field with @Enumerated(EnumType.STRING)
- [x] 2.5 Add createdAt and updatedAt with @PrePersist @PreUpdate

## 3. Repository

- [x] 3.1 Create PaymentRepository.java extending JpaRepository
- [x] 3.2 Add findByStatus() method for startup recovery

## 4. Verification

- [x] 4.1 Run the application and confirm tables are
  created in PostgreSQL
- [] 4.2 Check tables exist using IntelliJ database viewer


---

Dependencies:

### specs — Detailed specifications for the change
Path: specs/**/*.md (not yet completed)

### design — Technical design document with implementation details

```
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

<!-- Background and current state -->

## Goals / Non-Goals

**Goals:**
<!-- What this design aims to achieve -->

**Non-Goals:**
<!-- What is explicitly out of scope -->

## Decisions

<!-- Key design decisions and rationale -->

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
```


Save your response to: /Users/sabeenfatima/IdeaProjects/payment-processor/openspec/changes/payment-entity/tasks.md