package com.fooddeliveryapp.service;

import com.fooddeliveryapp.config.StripeProperties;
import com.fooddeliveryapp.entity.Order;
import com.fooddeliveryapp.entity.Payment;
import com.fooddeliveryapp.entity.enums.PaymentStatus;
import com.fooddeliveryapp.repository.PaymentRepo;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StripeService {

    private static final Logger log = LoggerFactory.getLogger(StripeService.class);

    private final StripeProperties stripeProperties;
    private final PaymentRepo paymentRepository;

    public String createPaymentIntent(Order order) {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (order.getTotalAmount() * 100))   // Stripe uses smallest unit (cents)
                    .setCurrency(stripeProperties.getCurrency().toLowerCase())
                    .setDescription("Order #" + order.getId() + " - Food Delivery")
                    .setMetadata(java.util.Map.of(
                            "orderId", order.getId().toString(),
                            "customerEmail", order.getCustomer().getEmail()
                    ))
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Save Payment record
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setStripePaymentIntentId(paymentIntent.getId());
            payment.setAmount(order.getTotalAmount());
            payment.setStatus(PaymentStatus.PENDING);
            paymentRepository.save(payment);

            log.info("Stripe PaymentIntent created for Order ID: {}", order.getId());

            return paymentIntent.getClientSecret();

        } catch (StripeException e) {
            log.error("Stripe PaymentIntent creation failed for Order ID: {}", order.getId(), e);
            throw new RuntimeException("Payment initialization failed. Please try again.");
        }
    }

    // For webhook handling (called asynchronously by Stripe)
    public void handlePaymentSuccess(String paymentIntentId) {
        // Implementation for webhook
        log.info("Payment successful for PaymentIntent: {}", paymentIntentId);
        // Update Payment & Order status here
    }
}