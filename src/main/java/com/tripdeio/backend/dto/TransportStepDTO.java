package com.tripdeio.backend.dto;

public class TransportStepDTO {
    private String instruction;
    private Integer durationMinutes;
    private Double price;

    public TransportStepDTO(String instruction, Integer durationMinutes, Double price) {
        this.instruction = instruction;
        this.durationMinutes = durationMinutes;
        this.price = price;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
