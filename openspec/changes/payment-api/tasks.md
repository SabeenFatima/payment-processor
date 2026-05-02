# Tasks: payment-api

## 1. DTOs

- [x] 1.1 Create PaymentRequest.java in dto package with amount, currency, description fields
- [x] 1.2 Add @NotNull validation on amount and currency fields
- [x] 1.3 Add @Positive validation on amount field
- [ ] 1.4 Create PaymentResponse.java in dto package with id, amount, currency, description, status, createdAt, updatedAt

## 2. Service

- [ ] 2.1 Create PaymentService.java in service package
- [ ] 2.2 Add receivePayment() method that saves Payment and OutboxMessage in one transaction
- [ ] 2.3 Add getPayment() method that finds payment by id and returns PaymentResponse
- [ ] 2.4 Add private mapToResponse() helper to convert Payment entity to PaymentResponse DTO

## 3. Controller

- [ ] 3.1 Create PaymentController.java in controller package
- [ ] 3.2 Add POST /payments endpoint returning 202 Accepted
- [ ] 3.3 Add GET /payments/{id} endpoint returning 200 OK
- [ ] 3.4 Add @Valid annotation on POST request body

## 4. Verification

- [ ] 4.1 Run the application and confirm it starts without errors
- [ ] 4.2 Test POST /payments with Postman or IntelliJ HTTP client
- [ ] 4.3 Test GET /payments/{id} and confirm status is RECEIVED
- [ ] 4.4 Test POST /payments with missing amount and confirm 400 response