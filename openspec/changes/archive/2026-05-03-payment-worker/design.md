# Design: payment-worker

## Context
The OutboxScheduler is now publishing payment IDs to RabbitMQ.
Now we need the PaymentWorker to consume those messages and actually
process the payments by calling the Mock Payment Service. The worker
is the final step in the payment processing pipeline. It updates
payment status from RECEIVED to IN_PROGRESS when it picks up the
message, then to COMPLETED or FAILED based on the Mock Payment
Service response. Manual acknowledgement ensures that if the worker
crashes mid-processing, RabbitMQ re-queues the message,and it gets
retried after restart.

## Goals / Non-Goals

**Goals:**
- Consume payment messages from payments.queue
- Update payment status to IN_PROGRESS before calling Mock Service
- Call Mock Payment Service which simulates 10ms to 2s delay
- Update payment status to COMPLETED on success or FAILED on error
- Use manual RabbitMQ acknowledgement for reliability
- Simulate 10% failure rate in Mock Payment Service
- Handle duplicate messages safely by checking current status

**Non-Goals:**
- No retry limit implementation in this change
- No email or notification on payment completion
- No changes to OutboxScheduler or PaymentService
- No real external payment gateway integration

## Decisions

**Manual acknowledgement over auto acknowledgement**
The worker manually sends ACK to RabbitMQ only after the payment
status is saved to DB. If the worker crashes before ACK, RabbitMQ
re-queues the message and another worker instance picks it up.
Auto acknowledgement would ACK as soon as the message is received —
if the worker then crashes, the payment is lost. Alternatives
considered: auto-ack — rejected because it loses payments on crash.

**Check current status before processing**
Before updating to IN_PROGRESS the worker checks if the payment
is already COMPLETED or FAILED. This handles duplicate messages
safely. If the same message is delivered twice, the second
delivery is ignored. This is called idempotency.

**Mock Payment Service as separate REST endpoint**
The mock service is implemented as a real REST endpoint in the
same application at /mock/payment-service/process/{id}. The
worker calls it via RestTemplate exactly like it would call a
real external service. Alternatives considered: calling a method
directly is rejected because it would not simulate real network
behavior and delays.

**10% failure rate in Mock Service**
The mock service randomly returns 500 error 10% of the time to
demonstrate FAILED status handling. This proves the system handles
failures correctly without needing a real external service to fail.

## Risks / Trade-offs

[Risk] Worker crashes after updating to IN_PROGRESS but before
calling Mock Service — payment stuck IN_PROGRESS forever
- Mitigation: StartupRecoveryService will handle this in the
next change by re-queuing IN_PROGRESS payments on restart.

[Risk] Same payment processed twice if duplicate message delivered
- Mitigation: Worker checks current status before processing.
If already COMPLETED or FAILED it skips and ACKs the message.

[Risk] Mock Service delay of up to 2 seconds blocks worker thread
- Mitigation: RabbitMQ listener runs on separate thread pool
so other messages can be processed concurrently by other workers.