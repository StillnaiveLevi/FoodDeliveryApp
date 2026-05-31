package com.fooddeliveryapp.dto;

import lombok.Data;

@Data
public class MenuItemResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private boolean available;
}