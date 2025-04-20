package com.tripdeio.backend.dto;

import com.tripdeio.backend.entity.Attraction;

import java.util.List;

public class RouteResponseDTO {
    private double distance; // in meters
    private long duration;   // in seconds
    private List<Attraction> places;
    private String polyline;

    // Constructors
    public RouteResponseDTO(double distance, long duration, List<Attraction> places, String polyline) {
        this.distance = distance;
        this.duration = duration;
        this.places = places;
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

    public List<Attraction> getPlaces() {
        return places;
    }

    public void setPlaces(List<Attraction> places) {
        this.places = places;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }
}
