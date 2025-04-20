package com.tripdeio.backend.dto;

import com.tripdeio.backend.entity.Attraction;

public class AttractionResponseDTO {
    private Long id;
    private String name;
    private String description;
    private double lat;
    private double lng;

    public static AttractionResponseDTO fromEntity(Attraction attraction) {
        AttractionResponseDTO dto = new AttractionResponseDTO();
        dto.setId(attraction.getId());
        dto.setName(attraction.getName());
        dto.setDescription(attraction.getDescription());
        dto.setLat(attraction.getLat());
        dto.setLng(attraction.getLng());
        return dto;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}

