package com.fooddeliveryapp.service;

import com.fooddeliveryapp.dto.OrderTrackingResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderWebSocketService {

    private static final Logger log = LoggerFactory.getLogger(OrderWebSocketService.class);

    private final SimpMessagingTemplate messagingTemplate;

    public void sendOrderUpdate(Long orderId, OrderTrackingResponse tracking) {
        // Send to customer
        messagingTemplate.convertAndSend("/topic/order/" + orderId, tracking);
        
        // Send to restaurant
        messagingTemplate.convertAndSend("/topic/restaurant/orders/" + tracking.getRestaurantName(), tracking);

        log.info("Real-time update sent for Order #{}", orderId);
    }
}