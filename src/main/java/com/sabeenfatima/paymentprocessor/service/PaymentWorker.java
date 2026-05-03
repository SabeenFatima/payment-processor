package com.sabeenfatima.paymentprocessor.service;

import com.rabbitmq.client.Channel;
import com.sabeenfatima.paymentprocessor.entity.Payment;
import com.sabeenfatima.paymentprocessor.entity.PaymentStatus;
import com.sabeenfatima.paymentprocessor.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentWorker {

    // Task 2.2
    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;

    // Task 2.3: listens to payments.queue with manual ack
    @RabbitListener(queues = "payments.queue")
    public void processPayment(
            String paymentId,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {

        log.info("Worker received payment: {}", paymentId);

        try {
            // Task 2.4: check payment exists
            Payment payment = paymentRepository.findById(paymentId)
                    .orElse(null);

            if (payment == null) {
                log.error("Payment not found: {}", paymentId);
                channel.basicAck(deliveryTag, false);
                return;
            }

            // Task 2.5: skip if already processed (idempotency)
            if (payment.getStatus() == PaymentStatus.COMPLETED ||
                    payment.getStatus() == PaymentStatus.FAILED) {
                log.info("Payment {} already processed with status: {}",
                        paymentId, payment.getStatus());
                channel.basicAck(deliveryTag, false);
                return;
            }

            // Task 2.6: update to IN_PROGRESS
            payment.setStatus(PaymentStatus.IN_PROGRESS);
            paymentRepository.save(payment);
            log.info("Payment {} status: IN_PROGRESS", paymentId);

            // Task 2.7: call Mock Payment Service
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://localhost:8080/mock/payment-service/process/"
                            + paymentId,
                    null,
                    String.class
            );

            // Task 2.8: update to COMPLETED on success
            if (response.getStatusCode().is2xxSuccessful()) {
                payment.setStatus(PaymentStatus.COMPLETED);
                log.info("Payment {} status: COMPLETED", paymentId);
            } else {
                // Task 2.9: update to FAILED on error
                payment.setStatus(PaymentStatus.FAILED);
                log.info("Payment {} status: FAILED", paymentId);
            }

            // Task 2.10: save final status
            paymentRepository.save(payment);

            // ACK only after DB is updated
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Error processing payment {}: {}",
                    paymentId, e.getMessage());
            try {
                // NACK - requeue the message for retry
                channel.basicNack(deliveryTag, false, true);
            } catch (Exception nackEx) {
                log.error("Failed to NACK message: {}", nackEx.getMessage());
            }
        }
    }
}
