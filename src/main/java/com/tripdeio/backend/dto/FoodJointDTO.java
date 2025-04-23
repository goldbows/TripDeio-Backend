package com.tripdeio.backend.dto;

import java.util.List;

public class FoodJointDTO {
    private Long id;
    private String name;
    private String description;
    private Double lat;
    private Double lng;
    private List<FoodItemDTO> foodItems;

    public FoodJointDTO(Long id, String name, String description, Double lat, Double lng, List<FoodItemDTO> foodItems) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.foodItems = foodItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public List<FoodItemDTO> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItemDTO> foodItems) {
        this.foodItems = foodItems;
    }
}
