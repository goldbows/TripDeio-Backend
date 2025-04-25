package com.tripdeio.backend.controller;

import com.tripdeio.backend.dto.ItineraryDTO;
import com.tripdeio.backend.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itinerary")
public class ItineraryController {

    @Autowired
    private ItineraryService itineraryService;

    @PostMapping("/generate")
    public ResponseEntity<ItineraryDTO> generateItinerary(
            @RequestParam Long startAttractionId,
            @RequestParam Long endAttractionId,
            @RequestBody List<Long> selectedAttractionIds) {
        try {
            ItineraryDTO itineraryDTO = itineraryService.generateItinerary(startAttractionId, endAttractionId, selectedAttractionIds);
            return ResponseEntity.ok(itineraryDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}