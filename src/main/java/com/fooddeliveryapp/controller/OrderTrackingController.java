package com.fooddeliveryapp.controller;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fooddeliveryapp.dto.OrderTrackingResponse;
import com.fooddeliveryapp.entity.enums.OrderStatus;
import com.fooddeliveryapp.service.OrderTrackingService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/orders/track")
@RequiredArgsConstructor
public class OrderTrackingController {
     private final OrderTrackingService orderTrackingService;

     @GetMapping("/{orderId}")
        public ResponseEntity<OrderTrackingResponse> trackOrder( 
            @PathVariable Long orderId, 
            Authentication authentication) {
                String userEmail = authentication.name();
                OrderTrackingResponse response = orderTrackingService.getOrderStatus(orderId, userEmail);
                return ResponseEntity.ok(response);
            }   
    //Restaurant/admin only endpoint to update order status
     @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        
        orderTrackingService.updateOrderStatus(orderId, OrderStatus.valueOf(status));
        return ResponseEntity.ok("Status updated successfully");
    }
     
}
