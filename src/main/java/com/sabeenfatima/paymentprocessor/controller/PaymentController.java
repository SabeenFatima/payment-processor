package com.sabeenfatima.paymentprocessor.controller;

import com.sabeenfatima.paymentprocessor.dto.PaymentRequest;
import com.sabeenfatima.paymentprocessor.dto.PaymentResponse;
import com.sabeenfatima.paymentprocessor.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j

public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> receivePayment(
            @Valid @RequestBody PaymentRequest request)
    {
        log.info("Payment request received: {} {}",
                request.getAmount(), request.getCurrency());
        PaymentResponse response = paymentService.receivePayment(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(
            @PathVariable String id)
    {
        PaymentResponse response = paymentService.getPayment(id);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
