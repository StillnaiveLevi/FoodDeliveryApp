package com.fooddeliveryapp.service;

import com.fooddeliveryapp.dto.OrderItemRequest;
import com.fooddeliveryapp.dto.OrderRequest;
import com.fooddeliveryapp.dto.OrderResponse;
import com.fooddeliveryapp.dto.OrderTrackingResponse;
import com.fooddeliveryapp.entity.*;
import com.fooddeliveryapp.entity.enums.OrderStatus;
import com.fooddeliveryapp.entity.enums.PaymentStatus;
import com.fooddeliveryapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepo orderRepository;
    private final MenuItemRepo menuItemRepository;
    private final RestaurantRepo restaurantRepository;
    private final UserRepo userRepository;
    private final PaymentRepo paymentRepository;

    private final RedisTemplate<String, Object> redisTemplate;
    private final StripeService stripeService;
    private final PromoCodeService promoCodeService;
    private final OrderWebSocketService webSocketService;   // For real-time updates

     // Main Method: Place Order with Transaction, Lock, Promo & Payment
     
    @Transactional
    public OrderResponse placeOrder(OrderRequest request, String userEmail) {
        String lockKey = "order:lock:restaurant:" + request.getRestaurantId();

        // Acquire distributed lock to prevent race conditions
        Boolean lockAcquired = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "LOCKED", 15, TimeUnit.SECONDS);

        if (Boolean.FALSE.equals(lockAcquired)) {
            throw new RuntimeException("Restaurant is currently busy. Please try again in a few seconds.");
        }

        try {
            // Fetch Customer
            User customerUser = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            Customer customer = (Customer) customerUser;

            // Fetch Restaurant
            Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));

            // Create Order
            Order order = new Order();
            order.setCustomer(customer);
            order.setRestaurant(restaurant);
            order.setDeliveryAddress(request.getDeliveryAddress());
            order.setDeliveryLatitude(request.getDeliveryLatitude());
            order.setDeliveryLongitude(request.getDeliveryLongitude());
            order.setStatus(OrderStatus.PENDING);
            order.setOrderDate(LocalDateTime.now());

            double totalAmount = 0.0;

            // Process Order Items
            for (OrderItemRequest itemReq : request.getItems()) {
                MenuItem menuItem = menuItemRepository.findById(itemReq.getMenuItemId())
                        .orElseThrow(() -> new RuntimeException("Menu item not found: ID " + itemReq.getMenuItemId()));

                if (!menuItem.isAvailable()) {
                    throw new RuntimeException(menuItem.getName() + " is currently unavailable.");
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setMenuItem(menuItem);
                orderItem.setQuantity(itemReq.getQuantity());
                orderItem.setUnitPrice(menuItem.getPrice());
                orderItem.setOrder(order);

                order.getItems().add(orderItem);
                totalAmount += menuItem.getPrice() * itemReq.getQuantity();
            }

            order.setTotalAmount(totalAmount);

            //  Promo Code Integration 
            Double finalAmount = totalAmount;
            PromoCode appliedPromo = null;

            if (request.getPromoCode() != null && !request.getPromoCode().trim().isEmpty()) {
                appliedPromo = promoCodeService.validatePromoCode(request.getPromoCode(), totalAmount);
                finalAmount = promoCodeService.applyDiscount(appliedPromo, totalAmount);
                order.setPromoCode(request.getPromoCode());
                log.info("Promo code {} applied on order. Discounted amount: {}", request.getPromoCode(), finalAmount);
            }

            order.setTotalAmount(finalAmount);

            // Save Order
            Order savedOrder = orderRepository.save(order);

            //  Create Payment with Stripe
            String clientSecret = stripeService.createPaymentIntent(savedOrder);

            // Mark payment record
            Payment payment = new Payment();
            payment.setOrder(savedOrder);
            payment.setAmount(finalAmount);
            payment.setStatus(PaymentStatus.PENDING);
            paymentRepository.save(payment);

            savedOrder.setPayment(payment);

            // Apply promo usage if used
            if (appliedPromo != null) {
                promoCodeService.incrementUsage(appliedPromo);
            }

            log.info("Order #{} placed successfully by user {} | Amount: ${}", 
                    savedOrder.getId(), userEmail, finalAmount);

            // Send real-time update
            webSocketService.sendOrderUpdate(savedOrder.getId(), 
                    buildTrackingResponse(savedOrder));

            // Build Response
            OrderResponse response = new OrderResponse();
            response.setOrderId(savedOrder.getId());
            response.setStatus(savedOrder.getStatus().name());
            response.setTotalPrice(finalAmount);
            response.setPaymentIntentClientSecret(clientSecret);
            response.setMessage("Order placed successfully!");

            return response;

        } finally {
            // Always release the lock
            redisTemplate.delete(lockKey);
        }
    }

    private OrderTrackingResponse buildTrackingResponse(Order order) {
        OrderTrackingResponse tracking = new OrderTrackingResponse();
        tracking.setOrderId(order.getId());
        tracking.setStatus(order.getStatus());
        tracking.setRestaurantName(order.getRestaurant().getName());
        tracking.setTotalAmount(order.getTotalAmount());
        tracking.setCurrentStatusMessage("Order received and being processed.");
        return tracking;
    }
}