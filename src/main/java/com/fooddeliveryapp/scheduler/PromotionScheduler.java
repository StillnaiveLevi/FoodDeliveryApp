package com.fooddeliveryapp.scheduler;

import com.fooddeliveryapp.service.PromoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionScheduler {

    private static final Logger log = LoggerFactory.getLogger(PromotionScheduler.class);

    private final PromoService promoService;

    // Run every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void expirePromoCodes() {
        log.info("Starting daily promo code expiration job...");
        promoService.expirePromoCodes();
        log.info("Promo code expiration job completed.");
    }

    // Run every 30 minutes to check pending orders
    @Scheduled(cron = "0 */30 * * * ?")
    public void cleanupPendingOrders() {
        log.info("Running pending order cleanup job...");
        // promoService or OrderService logic to auto-cancel old pending orders
    }

    // Daily report (example)
    @Scheduled(cron = "0 0 8 * * ?")   // Every day at 8 AM
    public void generateDailyReport() {
        log.info("Generating daily sales report...");
        // Add reporting logic
    }
}