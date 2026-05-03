# Tasks: outbox-pattern

## 1. Scheduler Setup

- [x] 1.1 Verify @EnableScheduling is present on PaymentApplication.java
- [ ] 1.2 Create OutboxScheduler.java in service package with @Component annotation

## 2. Scheduler Implementation

- [x] 2.1 Inject OutboxMessageRepository and RabbitTemplate via constructor
- [x] 2.2 Add publishOutboxMessages() method with @Scheduled(fixedDelayString) annotation
- [x] 2.3 Add @Transactional annotation on publishOutboxMessages() method
- [x] 2.4 Fetch all unsent messages using findBySentFalse()
- [x] 2.5 For each message publish paymentId to RabbitMQ exchange with routing key
- [x] 2.6 Mark each message as sent=true after successful publish
- [x] 2.7 Catch exceptions per message and log error without stopping other messages

## 3. Testing

- [ ] 3.1 Run app and send POST /payments
- [ ] 3.2 Check IntelliJ logs for "Published payment to queue" message
- [ ] 3.3 Open RabbitMQ UI and confirm message appears then disappears from payments.queue
- [ ] 3.4 Check outbox_messages table in DB and confirm sent=true for published messages
