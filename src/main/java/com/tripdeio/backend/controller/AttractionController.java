package com.tripdeio.backend.controller;

import com.tripdeio.backend.repository.AttractionRepository;
import com.tripdeio.backend.entity.Attraction;
import com.tripdeio.backend.service.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/attraction")
public class AttractionController {
    @Autowired
    private AttractionRepository attractionRepo;

    private final AttractionService attractionService;

    public AttractionController(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    @GetMapping("/all")
    public List<Attraction> getAttractions() {
        return attractionRepo.findAll();
    }
}