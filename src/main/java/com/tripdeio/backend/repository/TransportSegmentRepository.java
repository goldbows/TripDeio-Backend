package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.TransportSegment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportSegmentRepository extends JpaRepository<TransportSegment, Long> {
}