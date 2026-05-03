package com.sabeenfatima.paymentprocessor.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Random;

@RestController
@RequestMapping("/mock/payment-service")
@Slf4j
public class MockPaymentServiceController {
    private final Random random = new Random();

    @PostMapping("/process/{paymentId}")
    public ResponseEntity<String> processPayment(
            @PathVariable String paymentId) throws InterruptedException {

        // Task 1.3: random delay 10ms to 2000ms
        int delay = 10 + random.nextInt(1990);

        //For testing purpose I introduced a larger delay of 10s
        //int delay = 10000;

        log.info("Mock service processing payment {} with {}ms delay",
                paymentId, delay);
        Thread.sleep(delay);

        // Task 1.4 and 1.5: 90% success, 10% failure
        if (random.nextInt(10) < 9) {
            log.info("Mock service: payment {} SUCCESS", paymentId);
            return ResponseEntity.ok("SUCCESS");
        } else {
            log.info("Mock service: payment {} FAILED", paymentId);
            return ResponseEntity.status(500).body("FAILED");
        }
    }
}
