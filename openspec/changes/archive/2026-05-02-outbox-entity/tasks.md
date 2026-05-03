# Tasks: outbox-entity

## 1. Outbox Entity

- [x] 1.1 Create OutboxMessage.java in entity package with @Entity and @Table annotations
- [x] 1.2 Add UUID primary key with @GeneratedValue
- [x] 1.3 Add paymentId field referencing the related payment
- [x] 1.4 Add sent boolean field defaulting to false via @PrePersist
- [x] 1.5 Add createdAt timestamp with @PrePersist

## 2. Repository

- [x] 2.1 Create OutboxMessageRepository.java in repository package extending JpaRepository
- [x] 2.2 Add findBySentFalse() method for scheduler to find unpublished messages

## 3. Verification

- [x] 3.1 Run the application and confirm outbox_messages table is created in PostgreSQL
- [x] 3.2 Check table exists using IntelliJ database viewer