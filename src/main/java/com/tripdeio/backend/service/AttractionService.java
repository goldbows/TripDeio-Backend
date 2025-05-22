package com.tripdeio.backend.service;

import com.tripdeio.backend.dto.AttractionDTO;
import com.tripdeio.backend.entity.AppUser;
import com.tripdeio.backend.entity.Attraction;
import com.tripdeio.backend.entity.enums.AttractionStatus;
import com.tripdeio.backend.repository.AttractionRepository;
import com.tripdeio.backend.utils.SecurityUtil;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AttractionService {

    @Autowired
    private AttractionRepository attractionRepository;

    @Autowired
    private SecurityUtil securityUtil;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    /**
     * Create Attraction
     * @param attractionDTO DTO from UI
     * @return list of Attractions
     */
    public Attraction createAttraction(AttractionDTO attractionDTO) {
        AppUser currentUser = securityUtil.getCurrentUserId();
        Attraction attraction = new Attraction();
        attraction.setName(attractionDTO.getName());
        attraction.setDescription(attractionDTO.getDescription());
        attraction.setLat(attractionDTO.getLat());
        attraction.setLng(attractionDTO.getLng());
        attraction.setTicketPrice(attractionDTO.getTicketPrice());
        attraction.setVisitDurationMinutes(attractionDTO.getVisitDurationMinutes());
        attraction.setStatus(currentUser.isAdmin() ? AttractionStatus.APPROVED : AttractionStatus.PENDING);
        attraction.setSubmittedBy(currentUser);
        // Set geom using PostGIS
        attraction.setGeom(geometryFactory.createPoint(new Coordinate(attractionDTO.getLng(), attractionDTO.getLat())));
        return attractionRepository.save(attraction);
    }

    /**
     * Get All Attractions
     * @return list of Attractions
     */
    public List<AttractionDTO> findAll() {
        List<Attraction> allAttractions = attractionRepository.findAll();
        return allAttractions.stream()
                .map(AttractionDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Get Attractions by User
     * @return list of Attractions
     */
    public List<AttractionDTO> findByUserId() {
        AppUser currentUser = securityUtil.getCurrentUserId();
        Long userId = currentUser.getId();
        List<Attraction> attractionsByUser = attractionRepository.findBySubmittedByIdAndStatus(userId, AttractionStatus.APPROVED);
        return attractionsByUser.stream()
                .map(AttractionDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Get All Approved Attractions
     * @return list of Attractions
     */
    public List<AttractionDTO> findApprovedAttractions() {
        List<Attraction> allApprovedAttractions = attractionRepository.findByStatus(AttractionStatus.APPROVED);
        return allApprovedAttractions.stream()
                .map(AttractionDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Get All Pending Attractions
     * @return list of Attractions
     */
    public List<AttractionDTO> findPendingAttractions() {
        List<Attraction> allApprovedAttractions = attractionRepository.findByStatus(AttractionStatus.PENDING);
        return allApprovedAttractions.stream()
                .map(AttractionDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Update Attraction
     * @param attractionDTO DTO from UI
     * @return list of Attractions
     */
    public Attraction updateAttraction(AttractionDTO attractionDTO) {
        Attraction original = attractionRepository.findById(attractionDTO.getId())
                .orElseThrow(() -> new RuntimeException("Original attraction not found"));

        AppUser currentUser = securityUtil.getCurrentUserId();
        Long userId = currentUser.getId();

        if (!Objects.equals(original.getSubmittedBy().getId(), userId)) {
            throw new AccessDeniedException("You can only edit your own attractions.");
        }

        if (currentUser.isAdmin()) {
            updateAttractionFields(original, attractionDTO);
            original.setStatus(AttractionStatus.APPROVED);
            original.setLastModifiedAt(LocalDateTime.now());
            original.setApprovedBy(currentUser);
            return attractionRepository.save(original);
        } else {
            Attraction edited = new Attraction();
            updateAttractionFields(edited, attractionDTO);
            edited.setSubmittedBy(currentUser);
            edited.setStatus(AttractionStatus.PENDING);
            edited.setOriginalAttraction(original);
            edited.setLastModifiedAt(LocalDateTime.now());

            return attractionRepository.save(edited);
        }
    }

    /**
     * Approve edited Attraction
     * @param editedAttractionId Attraction ID
     */
    @Transactional
    public void approveEditedAttraction(Long editedAttractionId) {
        Attraction edited = attractionRepository.findById(editedAttractionId)
                .orElseThrow(() -> new RuntimeException("Edited attraction not found"));

        if (AttractionStatus.APPROVED.equals(edited.getStatus())) {
            throw new IllegalStateException("This attraction is already approved.");
        }
        AppUser adminUser = securityUtil.getCurrentUserId();

        if (edited.getOriginalAttraction() == null) {
            edited.setStatus(AttractionStatus.APPROVED);
            attractionRepository.save(edited);
        } else {
            Attraction original = edited.getOriginalAttraction();

            // Copy editable fields from edited to original
            original.setName(edited.getName());
            original.setDescription(edited.getDescription());
            original.setLat(edited.getLat());
            original.setLng(edited.getLng());
            original.setTicketPrice(edited.getTicketPrice());
            original.setVisitDurationMinutes(edited.getVisitDurationMinutes());
            original.setGeom(edited.getGeom());
            original.setLastModifiedAt(LocalDateTime.now());
            original.setApprovedBy(adminUser);

            attractionRepository.delete(edited);
            attractionRepository.save(original);
        }
    }

    /**
     * Reject edited Attraction
     * @param editedAttractionId Attraction ID
     */
    @Transactional
    public void rejectEditedAttraction(Long editedAttractionId) {
        Attraction edited = attractionRepository.findById(editedAttractionId)
                .orElseThrow(() -> new RuntimeException("Edited attraction not found"));

        if (edited.getOriginalAttraction() == null) {
            throw new IllegalStateException("Not an edited attraction.");
        }
        attractionRepository.delete(edited);
    }

    /**
     * Delete Attraction
     * @param editedAttractionId Attraction ID
     */
    @Transactional
    public void deleteAttraction(Long editedAttractionId) {
        Attraction edited = attractionRepository.findById(editedAttractionId)
                .orElseThrow(() -> new RuntimeException("Edited attraction not found"));
        attractionRepository.delete(edited);
    }


    /**
     * Delete Attraction
     * @param attraction Attraction Entity
     * @param dto Attraction DTO
     */
    private void updateAttractionFields(Attraction attraction, AttractionDTO dto) {
        attraction.setName(dto.getName());
        attraction.setDescription(dto.getDescription());
        attraction.setLat(dto.getLat());
        attraction.setLng(dto.getLng());
        attraction.setTicketPrice(dto.getTicketPrice());
        attraction.setVisitDurationMinutes(dto.getVisitDurationMinutes());
        attraction.setGeom(geometryFactory.createPoint(new Coordinate(dto.getLng(), dto.getLat())));
    }
}

