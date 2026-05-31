package com.fooddeliveryapp.controller;

import com.fooddeliveryapp.dto.OrderRequest;
import com.fooddeliveryapp.dto.OrderResponse;
import com.fooddeliveryapp.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody OrderRequest request,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        OrderResponse response = orderService.placeOrder(request, userEmail);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderStatus(@PathVariable Long orderId) {
        // Implementation for order tracking
        return ResponseEntity.ok().build(); // Expand later
    }
}