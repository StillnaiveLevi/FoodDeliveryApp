package com.fooddeliveryapp.service;

import com.fooddeliveryapp.dto.MenuItemResponse;
import com.fooddeliveryapp.entity.MenuItem;
import com.fooddeliveryapp.repository.MenuItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuItemRepo menuItemRepository;

    // Heavily Cached - Improves performance significantly
    @Cacheable(value = "restaurantMenu", key = "#restaurantId")
    public List<MenuItemResponse> getMenuByRestaurant(Long restaurantId) {
        List<MenuItem> items = menuItemRepository.findAvailableMenuItems(restaurantId);
        return items.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional
    @CacheEvict(value = "restaurantMenu", key = "#restaurantId")
    public void addMenuItem(MenuItem menuItem, Long restaurantId) {
        // Logic to associate with restaurant
        menuItemRepository.save(menuItem);
    }

    private MenuItemResponse convertToResponse(MenuItem item) {
        MenuItemResponse dto = new MenuItemResponse();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setPrice(item.getPrice());
        dto.setImageUrl(item.getImageUrl());
        return dto;
    }
}