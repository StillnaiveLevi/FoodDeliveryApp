package com.fooddeliveryapp.entity;

import java.time.LocalDateTime;

import com.fooddeliveryapp.entity.enums.PromoType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class PromoCode extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String code;

    private Double discountAmount;     // Fixed amount
    private Double discountPercentage; // Percentage discount

    @Enumerated(EnumType.STRING)
    private PromoType promoType;

    private Double minOrderAmount;
    private LocalDateTime validUntil;
    private boolean active = true;

    private Integer usageLimit = 100;        // Total uses allowed
    private Integer usedCount = 0;
}