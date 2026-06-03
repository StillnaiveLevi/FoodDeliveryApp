package com.fooddeliveryapp.dto;

import com.fooddeliveryapp.entity.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderTrackingResponse {
    private Long orderId;
    private OrderStatus status;
    private String restaurantName;
    private String customerName;
    private Double totalAmount;
    private LocalDateTime estimatedDeliveryTime;
    private String deliveryPartnerName;
    private String currentStatusMessage;
}