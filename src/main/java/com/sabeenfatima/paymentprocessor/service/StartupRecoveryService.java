package com.sabeenfatima.paymentprocessor.service;

import com.sabeenfatima.paymentprocessor.entity.OutboxMessage;
import com.sabeenfatima.paymentprocessor.entity.Payment;
import com.sabeenfatima.paymentprocessor.entity.PaymentStatus;
import com.sabeenfatima.paymentprocessor.repository.OutboxMessageRepository;
import com.sabeenfatima.paymentprocessor.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartupRecoveryService {

    // Task 1.2
    private final PaymentRepository paymentRepository;
    private final OutboxMessageRepository outboxMessageRepository;

    // Task 1.3: recoverOnStartup with EventListener
    // Task 1.4: transactional so reset + outbox are atomic
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void recoverOnStartup() {
        log.info("Running startup recovery...");

        // Task 1.5: find all stuck payments (InProgress)
        List<Payment> stuckPayments =
                paymentRepository.findByStatus(PaymentStatus.IN_PROGRESS);

        // Task 1.6: log how many found
        log.info("Found {} stuck IN_PROGRESS payments", stuckPayments.size());

        if (stuckPayments.isEmpty()) {
            log.info("No recovery needed. Starting normally.");
            return;
        }

        for (Payment payment : stuckPayments) {

            // Task 1.7: reset to RECEIVED
            payment.setStatus(PaymentStatus.RECEIVED);
            paymentRepository.save(payment);

            // Task 1.8:create new outbox message
            OutboxMessage outbox = OutboxMessage.builder()
                    .paymentId(payment.getId())
                    .build();
            outboxMessageRepository.save(outbox);

            // Task 1.9: log each recovered payment
            log.info("Recovered stuck payment: {}", payment.getId());
        }

        log.info("Startup recovery complete. {} payments re-queued",
                stuckPayments.size());
    }
}
