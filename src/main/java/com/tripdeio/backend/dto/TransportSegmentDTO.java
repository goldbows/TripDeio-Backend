package com.tripdeio.backend.dto;

public class TransportSegmentDTO {
    private Integer estimatedMinutes;
    private Double estimatedPrice;
    private Long fromStopId;
    private Long toStopId;
    private Long transportMethodId;
    private String fromStopType;
    private String toStopType;

    public Integer getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public void setEstimatedMinutes(Integer estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }

    public Double getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(Double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public Long getFromStopId() {
        return fromStopId;
    }

    public void setFromStopId(Long fromStopId) {
        this.fromStopId = fromStopId;
    }

    public Long getToStopId() {
        return toStopId;
    }

    public void setToStopId(Long toStopId) {
        this.toStopId = toStopId;
    }

    public Long getTransportMethodId() {
        return transportMethodId;
    }

    public void setTransportMethodId(Long transportMethodId) {
        this.transportMethodId = transportMethodId;
    }

    public String getFromStopType() {
        return fromStopType;
    }

    public void setFromStopType(String fromStopType) {
        this.fromStopType = fromStopType;
    }

    public String getToStopType() {
        return toStopType;
    }

    public void setToStopType(String toStopType) {
        this.toStopType = toStopType;
    }
}