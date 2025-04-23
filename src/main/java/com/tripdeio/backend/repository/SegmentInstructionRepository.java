package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.SegmentInstruction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SegmentInstructionRepository extends JpaRepository<SegmentInstruction, Long> {
    List<SegmentInstruction> findByFromWaypointItineraryId(Long itineraryId);
}
