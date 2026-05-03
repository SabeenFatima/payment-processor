# Design: startup-recovery

## Context
The PaymentWorker updates payment status to IN_PROGRESS before
calling the Mock Payment Service. If the application crashes at
this exact moment, the payment is stuck in IN_PROGRESS forever
because the worker never finished. After restart, the OutboxScheduler
only picks up outbox messages with sent=false — it does not know
about stuck IN_PROGRESS payments. This service runs once on startup
to find and recover those stuck payments before normal processing begins.

## Goals / Non-Goals

**Goals:**
- Run once automatically when application fully starts
- Find all payments with status IN_PROGRESS in the database
- Reset each stuck payment back to RECEIVED status
- Create a new OutboxMessage for each so OutboxScheduler republishes it
- Complete recovery before any new payments are accepted

**Non-Goals:**
- No recovery of FAILED payments — those are final
- No recovery of COMPLETED payments — those are final
- No changes to OutboxScheduler or PaymentWorker
- No manual trigger endpoint for recovery
- No limit on how many payments can be recovered

## Decisions

**ApplicationReadyEvent over @PostConstruct**
@EventListener(ApplicationReadyEvent.class) is used instead of
@PostConstruct because ApplicationReadyEvent fires after the entire
Spring context is ready including RabbitMQ and database connections.
@PostConstruct fires earlier during bean initialization when some
dependencies may not be ready yet. Alternatives considered:
@PostConstruct — rejected because database may not be connected yet.

**Reset to RECEIVED over directly re-queuing**
Stuck payments are reset to RECEIVED status instead of directly
publishing to RabbitMQ. This routes them through the normal
outbox flow as OutboxScheduler picks them up and publishes them.
This is safer because it reuses the same reliable path as new
payments. Alternatives considered: directly publishing to RabbitMQ
from recovery service — rejected because it bypasses the outbox
guarantee.

**Create new OutboxMessage over reusing existing**
A new OutboxMessage is created for each recovered payment instead
of finding and resetting the existing one. This is simpler and
safer — the existing outbox record may already be marked sent=true
if the scheduler published it before the crash. Creating a new
one guarantees the scheduler will pick it up.

**@Transactional for atomic recovery**
The entire recovery for each payment is wrapped in a transaction.
If resetting the payment or creating the outbox message fails,
both roll back and the payment stays IN_PROGRESS to be recovered
on the next restart.

## Risks / Trade-offs

[Risk] Payment processed twice if crash happens after Mock Service
responds but before status is saved
- Mitigation: PaymentWorker already handles this with idempotency