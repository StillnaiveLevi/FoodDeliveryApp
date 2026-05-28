package com.fooddeliveryapp.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.fooddeliveryapp.config.JwtTokenProvider;
import com.fooddeliveryapp.dto.AuthResponse;
import com.fooddeliveryapp.dto.RegisterRequest;
import com.fooddeliveryapp.entity.Customer;
import com.fooddeliveryapp.entity.Restaurant;
import com.fooddeliveryapp.entity.User;
import com.fooddeliveryapp.entity.enums.Role;
import com.fooddeliveryapp.repository.RestaurantRepo;
import com.fooddeliveryapp.repository.UserRepo;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepo userRepo;
    private final RestaurantRepo restaurantRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(RegisterRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user;

        if (request.getRole() == Role.RESTAURANT_OWNER) {
            user = new User();
        } else {
            user = new Customer();
        }

        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        User savedUser = userRepo.save(user);

        if (request.getRole() == Role.RESTAURANT_OWNER) {
            Restaurant restaurant = new Restaurant();
            restaurant.setName(request.getRestaurantName());
            restaurant.setAddress(request.getRestaurantAddress());
            restaurant.setLatitude(request.getLatitude());
            restaurant.setLongitude(request.getLongitude());
            restaurant.setOwner(savedUser);

            restaurantRepo.save(restaurant);
        }

        String token = jwtTokenProvider.generateToken(savedUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        response.setEmail(savedUser.getEmail());
        response.setFullName(savedUser.getFullName());
        response.setRole(savedUser.getRole());

        return response;
    }
}