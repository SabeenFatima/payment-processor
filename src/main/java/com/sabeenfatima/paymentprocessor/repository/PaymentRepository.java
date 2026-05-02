package com.sabeenfatima.paymentprocessor.repository;
import com.sabeenfatima.paymentprocessor.entity.PaymentStatus;
import com.sabeenfatima.paymentprocessor.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByStatus(PaymentStatus status);
}
