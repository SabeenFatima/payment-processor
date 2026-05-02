package com.sabeenfatima.paymentprocessor.repository;

import com.sabeenfatima.paymentprocessor.entity.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, String> {

    // Task 2.2 - used by scheduler to find messages not yet published
    List<OutboxMessage> findBySentFalse();
}
