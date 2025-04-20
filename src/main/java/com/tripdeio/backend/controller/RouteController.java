package com.tripdeio.backend.controller;

import com.tripdeio.backend.dto.RouteResponseDTO;
import com.tripdeio.backend.repository.AttractionRepository;
import com.tripdeio.backend.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/route")
public class RouteController {
    @Autowired
    private AttractionRepository attractionRepo;

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/nearby-route")
    public ResponseEntity<RouteResponseDTO> getAttractionsNearRoute(
            @RequestParam double startLat,
            @RequestParam double startLng,
            @RequestParam double endLat,
            @RequestParam double endLng) {

        RouteResponseDTO routeResponseDTO = routeService.findAttractionsNearRoute(startLat, startLng, endLat, endLng);
        return ResponseEntity.ok(routeResponseDTO);
    }
}