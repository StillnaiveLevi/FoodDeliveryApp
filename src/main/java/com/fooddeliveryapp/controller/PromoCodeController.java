package com.fooddeliveryapp.controller;

import com.fooddeliveryapp.entity.PromoCode;
import com.fooddeliveryapp.repository.PromoCodeRepo;
import com.fooddeliveryapp.service.PromoCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/promos")
@RequiredArgsConstructor
public class PromoCodeController {

    private final PromoCodeService promoCodeService;
    private final PromoCodeRepo promoCodeRepository;

    // Public - Validate promo during checkout
    @GetMapping("/validate/{code}")
    public ResponseEntity<PromoCode> validatePromo(
            @PathVariable String code,
            @RequestParam Double orderAmount) {
        
        PromoCode promo = promoCodeService.validatePromoCode(code, orderAmount);
        return ResponseEntity.ok(promo);
    }

    // Admin / Restaurant Owner - Create new promo
    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER')")
    @PostMapping
    public ResponseEntity<PromoCode> createPromo(@RequestBody PromoCode promoCode) {
        PromoCode saved = promoCodeService.createPromoCode(promoCode);
        return ResponseEntity.ok(saved);
    }

    // Get all active promos
    @GetMapping("/active")
    public ResponseEntity<List<PromoCode>> getActivePromos() {
        List<PromoCode> promos = promoCodeRepository.findByActiveTrueAndValidUntilAfter(LocalDateTime.now());
        return ResponseEntity.ok(promos);
    }
}