package com.fooddeliveryapp.dto;

import lombok.Data;

@Data
public class OrderResponse {
    private Long orderId;
    private String restaurantName;
    private String deliveryAddress;
    private Double totalPrice;
    private String status;
    private String paymentIntentClientSecret; //for Stripe payment processing
}
