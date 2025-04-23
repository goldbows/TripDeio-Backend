package com.tripdeio.backend.dto;

public class AttractionDetailsDTO {
    private Integer visitDurationMinutes;
    private Double ticketPrice;

    public AttractionDetailsDTO(Integer visitDurationMinutes, Double ticketPrice) {
        this.visitDurationMinutes = visitDurationMinutes;
        this.ticketPrice = ticketPrice;
    }

    public Integer getVisitDurationMinutes() {
        return visitDurationMinutes;
    }

    public void setVisitDurationMinutes(Integer visitDurationMinutes) {
        this.visitDurationMinutes = visitDurationMinutes;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
