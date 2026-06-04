package com.fooddeliveryapp.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fooddeliveryapp.entity.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "orders")
@Getter @Setter
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    private Double totalAmount;
    private String deliveryAddress;
    private Double deliveryLatitude;
    private Double deliveryLongitude;

    private LocalDateTime estimatedDeliveryTime;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    // For real-time tracking
    private String deliveryPartnerName;
    private String deliveryPartnerPhone;

    private LocalDateTime orderDate;

    private String promoCode;
}