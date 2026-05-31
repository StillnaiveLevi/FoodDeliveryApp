package com.fooddeliveryapp.service;

import com.fooddeliveryapp.dto.OrderRequest;
import com.fooddeliveryapp.dto.OrderResponse;
import com.fooddeliveryapp.entity.*;
import com.fooddeliveryapp.entity.enums.OrderStatus;
import com.fooddeliveryapp.repository.*;
import com.fooddeliveryapp.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepo orderRepository;
    private final MenuItemRepo menuItemRepository;
    private final RestaurantRepo restaurantRepository;
    private final UserRepo userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final StripeService stripeService;      
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public OrderResponse placeOrder(OrderRequest request, String userEmail) {
        // Get current user
        User customer = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Distributed Lock to prevent overselling
        String lockKey = "restaurant:lock:" + request.getRestaurantId();
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 10, TimeUnit.SECONDS);

        if (Boolean.FALSE.equals(locked)) {
            throw new RuntimeException("Restaurant is busy. Please try again.");
        }

        try {
            Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));

            Order order = new Order();
            order.setCustomer((Customer) customer);
            order.setRestaurant(restaurant);
            order.setDeliveryAddress(request.getDeliveryAddress());
            order.setStatus(OrderStatus.PENDING);

            double total = 0.0;

            for (var itemReq : request.getItems()) {
                MenuItem menuItem = menuItemRepository.findById(itemReq.getMenuItemId())
                        .orElseThrow(() -> new RuntimeException("Menu item not found"));

                if (!menuItem.isAvailable()) {
                    throw new RuntimeException(menuItem.getName() + " is not available");
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setMenuItem(menuItem);
                orderItem.setQuantity(itemReq.getQuantity());
                orderItem.setUnitPrice(menuItem.getPrice());
                orderItem.setOrder(order);

                order.getOrderItems().add(orderItem);
                total += menuItem.getPrice() * itemReq.getQuantity();
            }

            order.setTotalAmount(total);

            // Save Order
            Order savedOrder = orderRepository.save(order);

            // Create Stripe Payment Intent
            String paymentIntent = stripeService.createPaymentIntent(savedOrder);

            log.info("Order placed successfully. Order ID: {} by user: {}", savedOrder.getId(), userEmail);

            OrderResponse response = new OrderResponse();
            response.setOrderId(savedOrder.getId());
            response.setStatus(savedOrder.getStatus().name());
            response.setTotalAmount(total);
            response.setPaymentIntentClientSecret(paymentIntent);

            return response;

        } finally {
            redisTemplate.delete(lockKey);   // Release lock
        }
    }
}