package com.tripdeio.backend.dto;

import com.tripdeio.backend.entity.City;

public class CityDTO {
    private Long id;
    private String name;
    private Double lat;
    private Double lng;
    private String country;
    private String description;
    private String status;

    public CityDTO() {
    }

    public CityDTO(City city) {
        this.id = city.getId();
        this.name = city.getName();
        this.lat = city.getLat();
        this.lng = city.getLng();
        this.country = city.getCountry();
        this.description = city.getDescription();
        this.status = city.getStatus().name();
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}