package com.tripdeio.backend.service;

import com.tripdeio.backend.dto.AttractionResponseDTO;
import com.tripdeio.backend.entity.Attraction;
import com.tripdeio.backend.repository.AttractionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttractionService {

    private final AttractionRepository attractionRepository;

    public AttractionService(AttractionRepository attractionRepository) {
        this.attractionRepository = attractionRepository;
    }
}

