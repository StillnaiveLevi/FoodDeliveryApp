package com.fooddeliveryapp.entity;

import java.util.ArrayList;
import java.util.List;

import com.fooddeliveryapp.entity.enums.Category;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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

    @ManyToMany
    private List<Category> categories = new ArrayList<>();
}
