package com.fooddeliveryapp.entity;

import com.fooddeliveryapp.entity.enums.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Payment extends BaseEntity {
    
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String stripePaymentIntentId;
    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

}
