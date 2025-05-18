package com.tripdeio.backend.dto;

import com.tripdeio.backend.entity.Attraction;

public class AttractionDTO {
    private Long id;
    private String name;
    private String description;
    private Double lat;
    private Double lng;
    private Double ticketPrice;
    private Integer visitDurationMinutes;
    private String status;

    public AttractionDTO() {
    }

    public AttractionDTO(Attraction attraction) {
        this.id = attraction.getId();
        this.name = attraction.getName();
        this.description = attraction.getDescription();
        this.lat = attraction.getLat();
        this.lng = attraction.getLng();
        this.ticketPrice = attraction.getTicketPrice();
        this.visitDurationMinutes = attraction.getVisitDurationMinutes();
        this.status = attraction.getStatus().name();
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

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Integer getVisitDurationMinutes() {
        return visitDurationMinutes;
    }

    public void setVisitDurationMinutes(Integer visitDurationMinutes) {
        this.visitDurationMinutes = visitDurationMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}