package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.Attraction;
import com.tripdeio.backend.entity.enums.AttractionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    List<Attraction> findByStatus(AttractionStatus status);

    @Query(value = "SELECT * FROM attraction WHERE approved = true AND ST_DWithin(geom::geography, ST_GeomFromText(:routeWkt, 4326)::geography, 20000)", nativeQuery = true)
    List<Attraction> findNearRoute(@Param("routeWkt") String routeWkt);

    List<Attraction> findBySubmittedByIdAndStatus(Long userId, AttractionStatus status);

    List<Attraction> findByStatusIn(List<AttractionStatus> statuses);

}