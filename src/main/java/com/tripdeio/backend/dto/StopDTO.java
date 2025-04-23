package com.tripdeio.backend.dto;

public class StopDTO {
    private String stopType; // "city", "attraction", "food_joint"
    private Long stopId;     // ID of the stop in the corresponding table
    private Integer estimatedVisitMinutes; // Optional, how long user plans to stay

    // Constructors
    public StopDTO() {}

    public StopDTO(String stopType, Long stopId, Integer estimatedVisitMinutes) {
        this.stopType = stopType;
        this.stopId = stopId;
        this.estimatedVisitMinutes = estimatedVisitMinutes;
    }

    // Getters and Setters
    public String getStopType() {
        return stopType;
    }

    public void setStopType(String stopType) {
        this.stopType = stopType;
    }

    public Long getStopId() {
        return stopId;
    }

    public void setStopId(Long stopId) {
        this.stopId = stopId;
    }

    public Integer getEstimatedVisitMinutes() {
        return estimatedVisitMinutes;
    }

    public void setEstimatedVisitMinutes(Integer estimatedVisitMinutes) {
        this.estimatedVisitMinutes = estimatedVisitMinutes;
    }
}
