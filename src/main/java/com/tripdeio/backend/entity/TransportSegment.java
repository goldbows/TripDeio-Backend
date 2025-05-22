package com.tripdeio.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transport_segment")
public class TransportSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_stop_id")
    private Long fromStopId;

    @Column(name = "to_stop_id")
    private Long toStopId;

    @ManyToOne
    @JoinColumn(name = "transport_method_id")
    private TransportMode transportMode;

    @Column(name = "estimated_minutes")
    private Integer estimatedMinutes;

    @Column(name = "estimated_price")
    private Double estimatedPrice;

    @Column(name = "from_stop_type")
    private String fromStopType;

    @Column(name = "to_stop_type")
    private String toStopType;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public TransportMode getTransportMethod() {
        return transportMode;
    }

    public void setTransportMethod(TransportMode transportMode) {
        this.transportMode = transportMode;
    }

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