package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.FoodJointRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodJointRatingRepository extends JpaRepository<FoodJointRating, Long> {
}