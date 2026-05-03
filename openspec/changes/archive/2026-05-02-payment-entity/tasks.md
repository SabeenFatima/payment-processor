# Task: payment-entity

## 1. Enum

- [x] 1.1 Create PaymentStatus.java enum with four values:
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
- [x] 4.2 Check tables exist using IntelliJ database viewer
