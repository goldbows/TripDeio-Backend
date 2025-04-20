package com.tripdeio.backend.service;

import com.tripdeio.backend.repository.AttractionRepository;
import org.springframework.stereotype.Service;

@Service
public class AttractionService {

    private final AttractionRepository attractionRepository;

    public AttractionService(AttractionRepository attractionRepository) {
        this.attractionRepository = attractionRepository;
    }
}

