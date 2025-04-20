package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.FoodJointItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodJointItemRepository extends JpaRepository<FoodJointItem, Long> {
}