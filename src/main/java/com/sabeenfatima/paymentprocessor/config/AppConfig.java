package com.sabeenfatima.paymentprocessor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    // Task 2.2: used by PaymentWorker to call Mock Payment Service
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
