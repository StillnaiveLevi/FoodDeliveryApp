package com.fooddeliveryapp.repository;

import com.fooddeliveryapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {
}