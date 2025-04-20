package com.tripdeio.backend.dto;

import java.util.List;

public class AttractionResponseDTO {
    private Long id;
    private String name;
    private String description;
    private double lat;
    private double lng;
    private List<AttractionImageDTO> images;

    public AttractionResponseDTO (Long id, String name, String description, double lat, double lng, List<AttractionImageDTO> images) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.images = images;
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

    public List<AttractionImageDTO> getImages() {
        return images;
    }

    public void setImages(List<AttractionImageDTO> images) {
        this.images = images;
    }
}

