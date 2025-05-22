package com.tripdeio.backend.controller;

import com.tripdeio.backend.entity.TransportMode;
import com.tripdeio.backend.repository.TransportModeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transport-mode")
public class TransportModeController {

    @Autowired
    private TransportModeRepository transportModeRepository;

    /**
     * Get All Transport Modes
     * @return list of TransportMode
     */
    @GetMapping("/all")
    public ResponseEntity<List<TransportMode>> getAllTransportModes() {
        return ResponseEntity.ok(transportModeRepository.findAll());
    }
}