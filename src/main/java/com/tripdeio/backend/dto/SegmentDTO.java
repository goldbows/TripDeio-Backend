package com.tripdeio.backend.dto;

import java.util.List;

public class SegmentDTO {
    private AttractionResponseDTO fromAttraction;
    private AttractionResponseDTO toAttraction;
    private List<TransportOptionDTO> transportOptions;
    private AttractionDetailsDTO attractionDetails;
    private List<FoodJointDTO> nearbyFoodJoints;

    // Constructor
    public SegmentDTO(AttractionResponseDTO fromAttraction, AttractionResponseDTO toAttraction,
                      List<TransportOptionDTO> transportOptions, AttractionDetailsDTO attractionDetails,
                      List<FoodJointDTO> nearbyFoodJoints) {
        this.fromAttraction = fromAttraction;
        this.toAttraction = toAttraction;
        this.transportOptions = transportOptions;
        this.attractionDetails = attractionDetails;
        this.nearbyFoodJoints = nearbyFoodJoints;
    }

    public AttractionResponseDTO getFromAttraction() {
        return fromAttraction;
    }

    public void setFromAttraction(AttractionResponseDTO fromAttraction) {
        this.fromAttraction = fromAttraction;
    }

    public AttractionResponseDTO getToAttraction() {
        return toAttraction;
    }

    public void setToAttraction(AttractionResponseDTO toAttraction) {
        this.toAttraction = toAttraction;
    }

    public List<TransportOptionDTO> getTransportOptions() {
        return transportOptions;
    }

    public void setTransportOptions(List<TransportOptionDTO> transportOptions) {
        this.transportOptions = transportOptions;
    }

    public AttractionDetailsDTO getAttractionDetails() {
        return attractionDetails;
    }

    public void setAttractionDetails(AttractionDetailsDTO attractionDetails) {
        this.attractionDetails = attractionDetails;
    }

    public List<FoodJointDTO> getNearbyFoodJoints() {
        return nearbyFoodJoints;
    }

    public void setNearbyFoodJoints(List<FoodJointDTO> nearbyFoodJoints) {
        this.nearbyFoodJoints = nearbyFoodJoints;
    }
}
