package com.fooddeliveryapp.dto;

import com.fooddeliveryapp.entity.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank
    private String fullName;

    private String phoneNumber;
    private Role role;

    //For restaurants
    private String restaurantName;
    private String restaurantAddress;
    private Double latitude;
    private Double longitude;
    
}
