package com.sabeenfatima.paymentprocessor.dto;

import com.sabeenfatima.paymentprocessor.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String id;
    private BigDecimal amount;
    private String currency;
    private String description;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
