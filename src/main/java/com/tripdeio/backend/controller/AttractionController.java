package com.tripdeio.backend.controller;

import com.tripdeio.backend.dto.AttractionDTO;
import com.tripdeio.backend.entity.AppUser;
import com.tripdeio.backend.repository.AttractionRepository;
import com.tripdeio.backend.entity.Attraction;
import com.tripdeio.backend.service.AppUserService;
import com.tripdeio.backend.service.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private AttractionRepository attractionRepository;

    @Autowired
    private AttractionService attractionService;

    @Autowired
    private AppUserService appUserService;

    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            AppUser user = appUserService.findByEmail(userDetails.getUsername());
            return user.getId();
        }
        return null;
    }

    @GetMapping("/all")
    public List<Attraction> getAttractions() {
        return attractionRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Attraction> createAttraction(@RequestBody AttractionDTO attractionDTO) {
        // Assume user ID comes from authentication (replace with your logic)
        Long userId = getCurrentUserId();
        Attraction attraction = attractionService.createAttraction(attractionDTO, userId);
        return ResponseEntity.ok(attraction);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<Attraction>> getUserAttractions() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(attractionRepository.findBySubmittedBy_Id(userId));
    }

    @GetMapping("/all-approved")
    public ResponseEntity<List<Attraction>> getApprovedAttractions() {
        return ResponseEntity.ok(attractionRepository.findByApprovedTrue());
    }

    @PutMapping("/admin/attraction/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveAttraction(@PathVariable Long id) {
        Attraction a = attractionRepository.findById(id).orElseThrow();
        a.setApproved(true);
        a.setEdited(false);
        return ResponseEntity.ok(attractionRepository.save(a));
    }
}