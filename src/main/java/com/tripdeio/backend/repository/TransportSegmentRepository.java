package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.TransportSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface TransportSegmentRepository extends JpaRepository<TransportSegment, Long> {
    Collection<? extends TransportSegment> findByFromStopIdAndToStopId(Long fromStopId, Long toStopId);
    List<TransportSegment> findByFromStopIdAndFromStopTypeAndToStopIdAndToStopType(
            @Param("fromStopId") Long fromStopId,
            @Param("fromStopType") String fromStopType,
            @Param("toStopId") Long toStopId,
            @Param("toStopType") String toStopType);
}