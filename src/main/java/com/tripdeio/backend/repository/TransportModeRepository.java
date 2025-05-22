package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.TransportMode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportModeRepository extends JpaRepository<TransportMode, Long> {
}