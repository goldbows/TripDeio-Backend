package com.tripdeio.backend.controller;

import com.tripdeio.backend.dto.AttractionDTO;
import com.tripdeio.backend.repository.AttractionRepository;
import com.tripdeio.backend.entity.Attraction;
import com.tripdeio.backend.service.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/attraction")
public class AttractionController {
    @Autowired
    private AttractionRepository attractionRepository;

    @Autowired
    private AttractionService attractionService;

    @GetMapping("/all")
    public List<Attraction> getAttractions() {
        return attractionRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Attraction> createAttraction(@RequestBody AttractionDTO attractionDTO) {
        // Assume user ID comes from authentication (replace with your logic)
        Long userId = null; // TODO: Get from SecurityContextHolder if authenticated
        Attraction attraction = attractionService.createAttraction(attractionDTO, userId);
        return ResponseEntity.ok(attraction);
    }
}