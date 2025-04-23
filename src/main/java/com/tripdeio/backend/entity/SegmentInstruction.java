package com.tripdeio.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "segment_instruction")
public class SegmentInstruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_waypoint_id")
    private Waypoint fromWaypoint;

    @ManyToOne
    @JoinColumn(name = "to_waypoint_id")
    private Waypoint toWaypoint;

    @ManyToOne
    @JoinColumn(name = "transport_method_id")
    private TransportMethod transportMethod;

    @Lob
    private String instructions;

    @Column(columnDefinition = "jsonb")
    private String turnByTurnJson; // store as raw JSON

    private int durationMinutes;
    private BigDecimal distanceKm;
    private BigDecimal estimatedPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Waypoint getFromWaypoint() {
        return fromWaypoint;
    }

    public void setFromWaypoint(Waypoint fromWaypoint) {
        this.fromWaypoint = fromWaypoint;
    }

    public Waypoint getToWaypoint() {
        return toWaypoint;
    }

    public void setToWaypoint(Waypoint toWaypoint) {
        this.toWaypoint = toWaypoint;
    }

    public TransportMethod getTransportMethod() {
        return transportMethod;
    }

    public void setTransportMethod(TransportMethod transportMethod) {
        this.transportMethod = transportMethod;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getTurnByTurnJson() {
        return turnByTurnJson;
    }

    public void setTurnByTurnJson(String turnByTurnJson) {
        this.turnByTurnJson = turnByTurnJson;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
    }

    public BigDecimal getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(BigDecimal estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }
}
