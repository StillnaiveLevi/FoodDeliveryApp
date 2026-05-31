package com.fooddeliveryapp.service;

import com.fooddeliveryapp.entity.PromoCode;
import com.fooddeliveryapp.repository.PromoCodeRepo;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PromoService {

    private static final Logger log = LoggerFactory.getLogger(PromoService.class);
    private final PromoCodeRepo promoCodeRepository;

    @Cacheable(value = "promoCode", key = "#code")
    public PromoCode validatePromoCode(String code, Double orderAmount) {
        PromoCode promo = promoCodeRepository.findByCodeAndActiveTrue(code)
                .orElseThrow(() -> new RuntimeException("Invalid or expired promo code"));

        if (promo.getValidUntil().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Promo code has expired");
        }

        if (orderAmount < promo.getMinOrderAmount()) {
            throw new RuntimeException("Order amount does not meet minimum requirement");
        }

        return promo;
    }

    @Transactional
    public void applyPromoCode(PromoCode promo) {
        promo.setUsedCount(promo.getUsedCount() + 1);
        promoCodeRepository.save(promo);
    }

    // Cron Job - Auto expire promo codes
    @Scheduled(cron = "0 0 1 * * ?")   // Runs daily at 1 AM
    @Transactional
    public void expirePromoCodes() {
        log.info("Running promo code expiration job...");
        promoCodeRepository.expireOldPromoCodes(LocalDateTime.now());
        log.info("Promo code expiration completed.");
    }
}