# Tasks: payment-worker

## 1. Mock Payment Service

- [x] 1.1 Create MockPaymentServiceController.java in controller package
- [x] 1.2 Add POST /mock/payment-service/process/{paymentId} endpoint
- [x] 1.3 Add random delay between 10ms and 2000ms using Thread.sleep
- [x] 1.4 Return 200 OK with body "SUCCESS" 90% of the time
- [x] 1.5 Return 500 error with body "FAILED" 10% of the time

## 2. Payment Worker

- [x] 2.1 Create PaymentWorker.java in service package with @Component annotation
- [x] 2.2 Inject PaymentRepository and RestTemplate via constructor
- [x] 2.3 Add processPayment() method with @RabbitListener on payments.queue
- [x] 2.4 Check if payment exists and skip if not found
- [x] 2.5 Check current status — skip and ACK if already COMPLETED or FAILED
- [x] 2.6 Update payment status to IN_PROGRESS and save to DB
- [x] 2.7 Call Mock Payment Service via RestTemplate
- [x] 2.8 Update status to COMPLETED if response is 2xx
- [x] 2.9 Update status to FAILED if response is error or exception thrown
- [x] 2.10 Save final status to DB

## 3. RabbitMQ Manual Acknowledgement

- [x] 3.1 Add acknowledgement mode configuration to application.properties
- [ ] 3.2 Verify messages are removed from queue after processing

## 4. Testing

- [ ] 4.1 Run app and send POST /payments
- [ ] 4.2 Watch logs for IN_PROGRESS then COMPLETED or FAILED status updates
- [ ] 4.3 Call GET /payments/{id} and confirm final status is COMPLETED or FAILED
- [ ] 4.4 Send 10 payments and confirm roughly 9 COMPLETED and 1 FAILED