package com.sabeenfatima.paymentprocessor.service;

import com.sabeenfatima.paymentprocessor.config.RabbitMQConfig;
import com.sabeenfatima.paymentprocessor.entity.OutboxMessage;
import com.sabeenfatima.paymentprocessor.repository.OutboxMessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxScheduler {

    // Task 2.1: inject via constructor by @RequiredArgsConstructor
    private final OutboxMessageRepository outboxMessageRepository;
    private final RabbitTemplate rabbitTemplate;

    // Task 2.2: runs every 1 second, waits for previous run to finish
    // Task 2.3: transactional so sent=true is committed atomically
    @Scheduled(fixedDelayString = "${app.outbox.scheduler-delay}")
    @Transactional
    public void publishOutboxMessages() {

        // Task 2.4: fetch all unsent messages
        List<OutboxMessage> unsentMessages =
                outboxMessageRepository.findBySentFalse();

        if (!unsentMessages.isEmpty()) {
            log.info("Found {} unsent outbox messages", unsentMessages.size());
        }

        for (OutboxMessage message : unsentMessages) {
            try {
                // Task 2.5: publish payment id to RabbitMQ
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.PAYMENT_EXCHANGE,
                        RabbitMQConfig.PAYMENT_ROUTING_KEY,
                        message.getPaymentId()
                );

                // Task 2.6: mark as sent only after successful publish
                message.setSent(true);
                outboxMessageRepository.save(message);

                log.info("Published payment {} to queue", message.getPaymentId());

            } catch (Exception e) {
                // Task 2.7: log error but continue with other messages
                log.error("Failed to publish payment {}: {}",
                        message.getPaymentId(), e.getMessage());
                // message stays sent=false and retries next cycle
            }
        }
    }
}
