package com.tripdeio.backend.dto;

import java.util.List;

public class TransportOptionDTO {
    private String transportMethod;
    private List<TransportStepDTO> steps;

    public TransportOptionDTO(String transportMethod, List<TransportStepDTO> steps) {
        this.transportMethod = transportMethod;
        this.steps = steps;
    }

    public String getTransportMethod() {
        return transportMethod;
    }

    public void setTransportMethod(String transportMethod) {
        this.transportMethod = transportMethod;
    }

    public List<TransportStepDTO> getSteps() {
        return steps;
    }

    public void setSteps(List<TransportStepDTO> steps) {
        this.steps = steps;
    }
}
