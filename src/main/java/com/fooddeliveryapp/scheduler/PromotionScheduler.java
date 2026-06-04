package com.fooddeliveryapp.scheduler;

import com.fooddeliveryapp.service.PromoCodeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionScheduler {

    private static final Logger log = LoggerFactory.getLogger(PromotionScheduler.class);

    private final PromoCodeService promoCodeService;

    /**
     * Daily Promo Code Expiration Job
     * Runs every day at 1:00 AM
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void expirePromoCodes() {
        log.info("=== Starting Daily Promo Code Expiration Job ===");
        try {
            promoCodeService.expireOldPromoCodes();
            log.info("Promo code expiration job completed successfully.");
        } catch (Exception e) {
            log.error("Error occurred while expiring promo codes", e);
        }
    }

    /**
     * Clean up expired and inactive promos (optional deeper cleanup)
     * Runs every Sunday at 2:30 AM
     */
    @Scheduled(cron = "0 30 2 * * SUN")
    public void cleanupInactivePromos() {
        log.info("=== Starting Weekly Promo Cleanup Job ===");
        // You can add more logic here if needed (e.g., delete very old promos)
        log.info("Promo cleanup job completed.");
    }

    /**
     * Daily Report - Active Promotions Summary
     * Runs every day at 9:00 AM
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void generateDailyPromoReport() {
        log.info("=== Generating Daily Promotion Report ===");
        // You can inject PromoCodeRepository here to count active promos
        log.info("Daily promotion report generated.");
    }

    /**
     * Optional: Reset daily usage counters if you implement daily limits later
     */
    // @Scheduled(cron = "0 0 0 * * ?")
    // public void resetDailyUsage() { ... }
}