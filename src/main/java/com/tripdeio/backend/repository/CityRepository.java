package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.City;
import com.tripdeio.backend.entity.enums.AttractionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByStatus(AttractionStatus status);

    List<City> findBySubmittedByIdAndStatus(Long userId, AttractionStatus status);

    List<City> findByStatusIn(List<AttractionStatus> statuses);
}