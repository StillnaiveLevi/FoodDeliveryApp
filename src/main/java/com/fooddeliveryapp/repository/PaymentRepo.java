package com.fooddeliveryapp.repository;

import com.fooddeliveryapp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
    
    Payment findByStripePaymentIntentId(String stripePaymentIntentId);
}