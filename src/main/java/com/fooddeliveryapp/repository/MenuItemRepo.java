package com.fooddeliveryapp.repository;

import com.fooddeliveryapp.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepo extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantIdAndIsAvailableTrue(Long restaurantId);

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.isAvailable = true")
    List<MenuItem> findAvailableMenuItems(@Param("restaurantId") Long restaurantId);

    // For caching - frequently used query
    List<MenuItem> findByRestaurantId(Long restaurantId);
}