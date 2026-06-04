package com.fooddeliveryapp.service;

import com.fooddeliveryapp.entity.PromoCode;
import com.fooddeliveryapp.entity.enums.PromoType;
import com.fooddeliveryapp.repository.PromoCodeRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PromoCodeService {

    private static final Logger log = LoggerFactory.getLogger(PromoCodeService.class);

    private final PromoCodeRepo promoCodeRepository;

    @Cacheable(value = "promoCode", key = "#code")
    public PromoCode validatePromoCode(String code, Double orderAmount) {
        PromoCode promo = promoCodeRepository.findValidPromoWithLimit(code)
                .orElseThrow(() -> new RuntimeException("Invalid or expired promo code"));

        if (promo.getValidFrom() != null && promo.getValidFrom().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Promo code is not yet active");
        }

        if (promo.getValidUntil().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Promo code has expired");
        }

        if (orderAmount < promo.getMinOrderAmount()) {
            throw new RuntimeException("Minimum order amount for this promo is $" + promo.getMinOrderAmount());
        }

        return promo;
    }

    public Double applyDiscount(PromoCode promo, Double originalAmount) {
        return switch (promo.getPromoType()) {
            case FIXED_AMOUNT -> Math.max(0, originalAmount - promo.getDiscountAmount());
            case PERCENTAGE -> {
                double discount = originalAmount * (promo.getDiscountPercentage() / 100.0);
                yield Math.max(0, originalAmount - discount);
            }
            case FREE_DELIVERY -> originalAmount;   // Delivery fee handled separately
            case BUY_ONE_GET_ONE -> originalAmount; // Logic can be extended
        };
    }

    @Transactional
    public void incrementUsage(PromoCode promo) {
        promo.setUsedCount(promo.getUsedCount() + 1);

        if (promo.getUsedCount() >= promo.getUsageLimit()) {
            promo.setActive(false);
            log.info("Promo code {} reached usage limit and deactivated", promo.getCode());
        }

        promoCodeRepository.save(promo);
    }

    // Admin / Restaurant can create promos
    @CacheEvict(value = "promoCode", allEntries = true)
    public PromoCode createPromoCode(PromoCode promoCode) {
        if (promoCodeRepository.existsByCode(promoCode.getCode())) {
            throw new RuntimeException("Promo code already exists");
        }
        return promoCodeRepository.save(promoCode);
    }

    // Cron job support
    @Scheduled(cron = "0 0 1 * * ?")   // Daily at 1 AM
    @Transactional
    public void expireOldPromoCodes() {
        log.info("Running scheduled promo expiration job...");
        promoCodeRepository.expireOldPromoCodes(LocalDateTime.now());
        log.info("Promo expiration job completed.");
    }
}