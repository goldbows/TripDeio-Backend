package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}