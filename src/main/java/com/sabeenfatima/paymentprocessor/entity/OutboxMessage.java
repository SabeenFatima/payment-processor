package com.sabeenfatima.paymentprocessor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxMessage {

    // Task 1.2 - primary key
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // Task 1.3 - which payment this message belongs to
    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    // Task 1.4 - has this been published to RabbitMQ yet?
    @Column(nullable = false)
    private boolean sent;

    // Task 1.5 - when was this record created
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        sent = false;   // always starts as unsent
    }
}
