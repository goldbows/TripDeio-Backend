package com.tripdeio.backend.dto;

import java.util.List;

public class RouteResponseDTO {
    private double distance; // in meters
    private long duration;   // in seconds
    private List<AttractionResponseDTO> attractions;
    private String polyline;

    // Constructors
    public RouteResponseDTO(double distance, long duration, List<AttractionResponseDTO> attractions, String polyline) {
        this.distance = distance;
        this.duration = duration;
        this.attractions = attractions;
        this.polyline = polyline;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public List<AttractionResponseDTO> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<AttractionResponseDTO> attractionResponseDTOS) {
        this.attractions = attractionResponseDTOS;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }
}
