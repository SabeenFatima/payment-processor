# Design: outbox-pattern

## Context
The REST API saves payments and outbox records together in one
transaction. Now we need the OutboxScheduler to pick up those
unsent outbox records and publish them to RabbitMQ. This scheduler
is the bridge between the database and the message queue. Without
it, payments would be saved to DB but never processed. The outbox
pattern guarantees that even if the app crashes after saving but
before publishing, the message will be published on the next
scheduler run after restart.

## Goals / Non-Goals

**Goals:**
- Poll outbox_messages table every second for unsent messages
- Publish each unsent message to RabbitMQ exchange
- Mark message as sent=true after successful publish
- Leave message as sent=false if publish fails so it retries
- Keep scheduling enabled via @EnableScheduling on main class

**Non-Goals:**
- No consuming of messages in this change (that is PaymentWorker)
- No payment status updates in this change
- No retry limit or dead letter handling in this change
- No changes to Payment or OutboxMessage entities

## Decisions

**@Scheduled polling over event-driven publishing**
The scheduler polls the outbox table every second instead of
publishing directly when a payment is saved. This means even if
the app crashes right after saving, the outbox record survives in
the DB and gets picked up after restart. Alternatives considered:
publishing directly from PaymentService — rejected because if the
app crashes between save and publish the message is lost forever.

**Fixed delay over fixed rate**
@Scheduled(fixedDelayString) is used instead of fixedRate. Fixed
delay waits for the previous execution to finish before starting
the next one. Fixed rate could cause overlapping executions if
publishing takes longer than 1 second, leading to duplicate
messages. Fixed delay is safer.

**Mark sent=true per message not in batch**
Each message is marked sent=true individually right after its
own successful publish. This means if the app crashes mid-batch,
only already-published messages are marked sent. Unpublished ones
remain sent=false and get retried. Alternatives considered: batch
update after all publishes — rejected because a crash mid-batch
would leave some published messages unmarked causing re-publishing.

**@Transactional on scheduler method**
The scheduler method is marked @Transactional so that marking
a message as sent=true is committed to the DB atomically. If
the transaction fails, the sent flag stays false and the message
will be retried on the next cycle.

## Risks / Trade-offs

[Risk] Same message published twice if app crashes after publish
but before marking sent=true
- Mitigation: PaymentWorker must handle duplicate messages
by checking current payment status before processing.

[Risk] Scheduler runs on every instance when horizontally scaled
- Mitigation: Each instance will try to publish the same unsent
messages. This is handled by marking sent=true immediately after publish.
So, whichever instance marks it first wins. In production,
database-level locking with SELECT FOR UPDATE SKIP LOCKED
would be used to prevent duplicates entirely.

[Risk] Outbox table grows large with sent=true records over time
- Mitigation: Acceptable for this assignment. In production a
cleanup job would delete old sent records periodically.