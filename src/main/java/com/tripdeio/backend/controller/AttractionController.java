package com.tripdeio.backend.controller;

import com.tripdeio.backend.dto.AttractionDTO;
import com.tripdeio.backend.repository.AttractionRepository;
import com.tripdeio.backend.entity.Attraction;
import com.tripdeio.backend.service.AppUserService;
import com.tripdeio.backend.service.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/attraction")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;

    /**
     * Create Attraction
     * @param attractionDTO DTO from UI
     * @return list of Attractions
     */
    @PostMapping
    public ResponseEntity<AttractionDTO> createAttraction(@RequestBody AttractionDTO attractionDTO) {
        Attraction attraction = attractionService.createAttraction(attractionDTO);
        return ResponseEntity.ok(new AttractionDTO(attraction));
    }

    /**
     * Get All Attractions
     * @return list of Attractions
     */
    @GetMapping("/all")
    public ResponseEntity<List<AttractionDTO>> getAllAttractions() {
        return ResponseEntity.ok(attractionService.findAll());
    }

    /**
     * Get Attractions by User
     * @return list of Attractions
     */
    @GetMapping("/by-user")
    public ResponseEntity<List<AttractionDTO>> getAttractionsByUser() {
        return ResponseEntity.ok(attractionService.findByUserId());
    }

    /**
     * Get All Approved Attractions
     * @return list of Attractions
     */
    @GetMapping("/all-approved")
    public ResponseEntity<List<AttractionDTO>> getApprovedAttractions() {
        return ResponseEntity.ok(attractionService.findApprovedAttractions());
    }

    /**
     * Get All Pending Attractions
     * @return list of Attractions
     */
    @GetMapping("/all-pending")
    public ResponseEntity<List<AttractionDTO>> getPendingAttractions() {
        return ResponseEntity.ok(attractionService.findPendingAttractions());
    }

    /**
     * Update Attraction
     * @param attractionDTO DTO from UI
     * @return list of Attractions
     */
    @PutMapping("/update")
    public ResponseEntity<AttractionDTO> updateAttraction(@RequestBody AttractionDTO attractionDTO) {
        Attraction attraction = attractionService.updateAttraction(attractionDTO);
        return ResponseEntity.ok(new AttractionDTO(attraction));
    }

    /**
     * Approve Attraction Update
     * @return Response
     */
    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveAttraction(@PathVariable Long id) {
        attractionService.approveEditedAttraction(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Reject Attraction Update
     * @return Response
     */
    @PutMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectAttraction(@PathVariable Long id) {
        attractionService.rejectEditedAttraction(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete Attraction
     * @return Response
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAttraction(@PathVariable Long id) {
        attractionService.deleteAttraction(id);
        return ResponseEntity.ok().build();
    }
}