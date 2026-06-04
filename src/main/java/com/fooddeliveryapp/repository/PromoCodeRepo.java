package com.fooddeliveryapp.repository;

import com.fooddeliveryapp.entity.PromoCode;
import com.fooddeliveryapp.entity.enums.PromoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromoCodeRepo extends JpaRepository<PromoCode, Long> {

    Optional<PromoCode> findByCodeAndActiveTrue(String code);

    boolean existsByCode(String code);

    // Find all active promos for a restaurant or global
    List<PromoCode> findByActiveTrueAndValidUntilAfter(LocalDateTime now);

    // For admin dashboard
    List<PromoCode> findByPromoType(PromoType promoType);

    // Cron job support - expire old promos
    @Modifying
    @Transactional
    @Query("UPDATE PromoCode p SET p.active = false " +
           "WHERE p.validUntil < :currentTime AND p.active = true")
    void expireOldPromoCodes(@Param("currentTime") LocalDateTime currentTime);

    // Check usage limit
    @Query("SELECT p FROM PromoCode p WHERE p.code = :code " +
           "AND p.active = true AND p.usedCount < p.usageLimit")
    Optional<PromoCode> findValidPromoWithLimit(@Param("code") String code);
}