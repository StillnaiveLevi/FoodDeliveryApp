package com.fooddeliveryapp.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {
    @NotNull 
    private Long restaurantId;

    @NotEmpty
    private List<OrderItemRequest> items;

    private String deliveryAddress;
    private Double deliveryLatitude;
    private Double deliveryLongitude; 
    
    private String promoCode;
}


