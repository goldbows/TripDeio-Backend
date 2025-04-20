package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    List<Itinerary> findByAppUserId(Long userId);
}