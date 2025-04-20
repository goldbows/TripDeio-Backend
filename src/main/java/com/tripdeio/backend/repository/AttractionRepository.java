package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    List<Attraction> findByApprovedTrue();
}