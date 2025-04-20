package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.TransportMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportMethodRepository extends JpaRepository<TransportMethod, Long> {
}