# Tasks: startup-recovery

## 1. Startup Recovery Service

- [x] 1.1 Create StartupRecoveryService.java in service package with @Component annotation
- [x] 1.2 Inject PaymentRepository and OutboxMessageRepository via constructor
- [x] 1.3 Add recoverOnStartup() method with @EventListener(ApplicationReadyEvent.class)
- [x] 1.4 Add @Transactional annotation on recoverOnStartup() method
- [x] 1.5 Find all payments with status IN_PROGRESS using findByStatus()
- [x] 1.6 Log how many stuck payments were found
- [x] 1.7 For each stuck payment reset status to RECEIVED and save to DB
- [x] 1.8 For each stuck payment create new OutboxMessage and save to DB
- [x] 1.9 Log each recovered payment id

## 2. Testing

- [ ] 2.1 Run app and send POST /payments
- [ ] 2.2 Wait for payment to reach IN_PROGRESS in logs
- [ ] 2.3 Force stop the app immediately using red stop button in IntelliJ
- [ ] 2.4 Check DB — payment should still be IN_PROGRESS
- [ ] 2.5 Restart the app
- [ ] 2.6 Check logs for "Found X stuck IN_PROGRESS payments"
- [ ] 2.7 Check logs for "Recovered payment {id}"
- [ ] 2.8 Wait and call GET /payments/{id} — should eventually reach COMPLETED or FAILED