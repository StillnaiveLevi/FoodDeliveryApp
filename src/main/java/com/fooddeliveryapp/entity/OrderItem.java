package com.fooddeliveryapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter 
public class OrderItem extends BaseEntity {
   
    @ManyToOne
    private Order order;

    @ManyToOne
    private MenuItem menuItem;

    private Integer quantity;
    private Double unitPrice;
}
