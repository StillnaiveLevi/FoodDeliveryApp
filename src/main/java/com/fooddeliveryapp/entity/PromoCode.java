package com.fooddeliveryapp.entity;

import com.fooddeliveryapp.entity.enums.PromoType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "promo_codes")
public class PromoCode extends BaseEntity {

    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Enumerated(EnumType.STRING)
    private PromoType promoType;

    // For FIXED_AMOUNT
    private Double discountAmount;

    // For PERCENTAGE
    private Double discountPercentage;

    private Double minOrderAmount = 0.0;

    private LocalDateTime validFrom;
    private LocalDateTime validUntil;

    private boolean active = true;

    private Integer usageLimit = 100;
    private Integer usedCount = 0;

    // If null → Global promo. Otherwise, restaurant-specific
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private String description;

    
}