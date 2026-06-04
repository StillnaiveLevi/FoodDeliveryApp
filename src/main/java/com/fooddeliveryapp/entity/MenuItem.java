package com.fooddeliveryapp.entity;

import java.util.Set;

import com.fooddeliveryapp.entity.enums.Category;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class MenuItem extends BaseEntity{
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private boolean isAvailable = true;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<Category> categories;
    }
