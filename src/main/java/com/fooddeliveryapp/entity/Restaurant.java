package com.fooddeliveryapp.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Restaurant extends BaseEntity {
    private String name;
    private String description;
    private String address;
    private double latitude;
    private double longitude;

    private boolean isOpen = true;
    private Double rating = 0.0;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<MenuItem> menuItems = new ArrayList<>();
}
