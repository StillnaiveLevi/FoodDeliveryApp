package com.fooddeliveryapp.service;

import com.fooddeliveryapp.dto.OrderTrackingResponse;
import com.fooddeliveryapp.entity.Order;
import com.fooddeliveryapp.entity.enums.OrderStatus;
import com.fooddeliveryapp.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderTrackingService {

    private static final Logger log = LoggerFactory.getLogger(OrderTrackingService.class);

    private final OrderRepo orderRepository;

    @Cacheable(value = "orderTracking", key = "#orderId")
    @Transactional(readOnly = true)
    public OrderTrackingResponse getOrderStatus(Long orderId, String userEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Security check
        if (!order.getCustomer().getEmail().equals(userEmail) && 
            !order.getRestaurant().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized access to order");
        }

        OrderTrackingResponse response = new OrderTrackingResponse();
        response.setOrderId(order.getId());
        response.setStatus(order.getStatus());
        response.setRestaurantName(order.getRestaurant().getName());
        response.setTotalAmount(order.getTotalAmount());
        response.setEstimatedDeliveryTime(order.getEstimatedDeliveryTime());

        response.setCurrentStatusMessage(getStatusMessage(order.getStatus()));

        return response;
    }

    @Transactional
    @CacheEvict(value = "orderTracking", key = "#orderId")
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(newStatus);

        if (newStatus == OrderStatus.OUT_FOR_DELIVERY) {
            order.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(35));
        }

        orderRepository.save(order);

        log.info("Order #{} status updated to {}", orderId, newStatus);
        
        // TODO: Push notification / WebSocket event here
    }

    private String getStatusMessage(OrderStatus status) {
        return switch (status) {
            case PENDING -> "Order received. Waiting for restaurant confirmation.";
            case CONFIRMED -> "Restaurant confirmed your order.";
            case PREPARING -> "Your food is being prepared.";
            case OUT_FOR_DELIVERY -> "Your order is on the way!";
            case DELIVERED -> "Order delivered successfully. Enjoy your meal!";
            case CANCELLED -> "Order was cancelled.";
            default -> "Status updating...";
        };
    }
}