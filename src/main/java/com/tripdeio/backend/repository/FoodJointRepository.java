package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.FoodJoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodJointRepository extends JpaRepository<FoodJoint, Long> {
    List<FoodJoint> findByApprovedTrue();
}
