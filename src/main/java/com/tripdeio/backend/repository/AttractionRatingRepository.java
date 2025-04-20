package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.AttractionRating;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AttractionRatingRepository extends JpaRepository<AttractionRating, Long> {
}