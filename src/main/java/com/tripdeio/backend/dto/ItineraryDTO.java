package com.tripdeio.backend.dto;

import java.util.List;

public class ItineraryDTO {
    private Long itineraryId;
    private AttractionResponseDTO startPoint;
    private AttractionResponseDTO endPoint;
    private List<SegmentDTO> segments;

    // Constructor
    public ItineraryDTO(Long itineraryId, AttractionResponseDTO startPoint, AttractionResponseDTO endPoint, List<SegmentDTO> segments) {
        this.itineraryId = itineraryId;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.segments = segments;
    }

    // Getters and Setters
    public Long getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(Long itineraryId) {
        this.itineraryId = itineraryId;
    }

    public AttractionResponseDTO getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(AttractionResponseDTO startPoint) {
        this.startPoint = startPoint;
    }

    public AttractionResponseDTO getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(AttractionResponseDTO endPoint) {
        this.endPoint = endPoint;
    }

    public List<SegmentDTO> getSegments() {
        return segments;
    }

    public void setSegments(List<SegmentDTO> segments) {
        this.segments = segments;
    }
}