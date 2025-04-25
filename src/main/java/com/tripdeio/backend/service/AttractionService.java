package com.tripdeio.backend.service;

import com.tripdeio.backend.dto.AttractionDTO;
import com.tripdeio.backend.entity.AppUser;
import com.tripdeio.backend.entity.Attraction;
import com.tripdeio.backend.repository.AttractionRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttractionService {

    @Autowired
    private AttractionRepository attractionRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    public Attraction createAttraction(AttractionDTO attractionDTO, Long userId) {
        Attraction attraction = new Attraction();
        attraction.setName(attractionDTO.getName());
        attraction.setDescription(attractionDTO.getDescription());
        attraction.setLat(attractionDTO.getLat());
        attraction.setLng(attractionDTO.getLng());
        attraction.setTicketPrice(attractionDTO.getTicketPrice());
        attraction.setVisitDurationMinutes(attractionDTO.getVisitDurationMinutes());
        attraction.setApproved(false); // Requires admin approval
        if (userId != null) {
            AppUser user = new AppUser();
            user.setId(userId);
            attraction.setSubmittedBy(user);
        }
        // Set geom using PostGIS
        attraction.setGeom(geometryFactory.createPoint(new Coordinate(attractionDTO.getLng(), attractionDTO.getLat())));
        return attractionRepository.save(attraction);
    }
}

