package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.ItineraryStop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryStopRepository extends JpaRepository<ItineraryStop, Long> {
}