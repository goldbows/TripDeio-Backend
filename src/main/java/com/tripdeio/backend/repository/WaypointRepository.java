package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.Waypoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaypointRepository extends JpaRepository<Waypoint, Long> {
    List<Waypoint> findByItineraryIdOrderByVisitOrder(Long itineraryId);
}
