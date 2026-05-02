package com.sabeenfatima.paymentprocessor.service;

import com.sabeenfatima.paymentprocessor.dto.PaymentRequest;
import com.sabeenfatima.paymentprocessor.dto.PaymentResponse;

import com.sabeenfatima.paymentprocessor.entity.OutboxMessage;
import com.sabeenfatima.paymentprocessor.entity.Payment;
import com.sabeenfatima.paymentprocessor.entity.PaymentStatus;

import com.sabeenfatima.paymentprocessor.repository.OutboxMessageRepository;
import com.sabeenfatima.paymentprocessor.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OutboxMessageRepository outboxMessageRepository;

    @Transactional
    public PaymentResponse receivePayment(PaymentRequest request)
    {
        // save payment
        Payment payment = Payment.builder()
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .description(request.getDescription())
                .status(PaymentStatus.RECEIVED)
                .build();
        payment = paymentRepository.save(payment);
        log.info("Payment saved: {}", payment.getId());

        // save outbox in same transaction
        OutboxMessage outbox = OutboxMessage.builder()
                .paymentId(payment.getId())
                .build();
        outboxMessageRepository.save(outbox);
        log.info("Outbox saved for payment: {}", payment.getId());
        return mapToResponse(payment);
    }

    public PaymentResponse getPayment(String id)
    {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found: " + id));
        return mapToResponse(payment);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .description(payment.getDescription())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
