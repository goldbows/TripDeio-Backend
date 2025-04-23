package com.tripdeio.backend.dto;

import java.math.BigDecimal;

public class GraphHopperRoute {
    private BigDecimal distance; // in KM
    private int duration; // in minutes
    private String instructions;
    private String json; // raw turn-by-turn JSON

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
