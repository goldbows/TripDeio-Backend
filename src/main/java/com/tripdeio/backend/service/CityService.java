package com.tripdeio.backend.service;

import com.tripdeio.backend.dto.CityDTO;
import com.tripdeio.backend.entity.AppUser;
import com.tripdeio.backend.entity.City;
import com.tripdeio.backend.entity.enums.AttractionStatus;
import com.tripdeio.backend.repository.CityRepository;
import com.tripdeio.backend.utils.SecurityUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;
    
    @Autowired
    private SecurityUtil securityUtil;

    /**
     * Create City
     * @param cityDTO from UI
     * @return list of Cities
     */
    public City createCity(CityDTO cityDTO) {
        AppUser currentUser = securityUtil.getCurrentUserId();
        City city = new City();
        city.setName(cityDTO.getName());
        city.setLat(cityDTO.getLat());
        city.setLng(cityDTO.getLng());
        city.setCountry(cityDTO.getCountry());
        city.setDescription(cityDTO.getDescription());
        city.setStatus(currentUser.isAdmin() ? AttractionStatus.APPROVED : AttractionStatus.PENDING);
        city.setSubmittedBy(currentUser);
        return cityRepository.save(city);
    }

    /**
     * Get All Cities
     * @return list of Cities
     */
    public List<CityDTO> findAll() {
        List<City> allCities = cityRepository.findAll();
        return allCities.stream()
                .map(CityDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Get Cities by User
     * @return list of Cities
     */
    public List<CityDTO> findByUserId() {
        AppUser currentUser = securityUtil.getCurrentUserId();
        Long userId = currentUser.getId();
        List<City> citiesByUser = cityRepository.findBySubmittedByIdAndStatus(userId, AttractionStatus.APPROVED);
        return citiesByUser.stream()
                .map(CityDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Get All Approved Cities
     * @return list of Cities
     */
    public List<CityDTO> findApprovedCities() {
        List<City> allApprovedCities = cityRepository.findByStatus(AttractionStatus.APPROVED);
        return allApprovedCities.stream()
                .map(CityDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Get All Pending Cities
     * @return list of Cities
     */
    public List<CityDTO> findPendingCities() {
        List<City> allApprovedCities = cityRepository.findByStatus(AttractionStatus.PENDING);
        return allApprovedCities.stream()
                .map(CityDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Update City
     * @param cityDTO DTO from UI
     * @return list of Cities
     */
    public City updateCity(CityDTO cityDTO) {
        City original = cityRepository.findById(cityDTO.getId())
                .orElseThrow(() -> new RuntimeException("Original city not found"));

        AppUser currentUser = securityUtil.getCurrentUserId();
        Long userId = currentUser.getId();

        if (!currentUser.isAdmin() && !Objects.equals(original.getSubmittedBy().getId(), userId)) {
            throw new AccessDeniedException("You can only edit your own cities.");
        }

        if (currentUser.isAdmin()) {
            updateCityFields(original, cityDTO);
            original.setStatus(AttractionStatus.APPROVED);
            original.setLastModifiedAt(LocalDateTime.now());
            original.setApprovedBy(currentUser);
            return cityRepository.save(original);
        } else {
            City edited = new City();
            updateCityFields(edited, cityDTO);
            edited.setSubmittedBy(currentUser);
            edited.setStatus(AttractionStatus.PENDING);
            edited.setOriginalCity(original);
            edited.setLastModifiedAt(LocalDateTime.now());

            return cityRepository.save(edited);
        }
    }

    /**
     * Approve edited City
     * @param editedCityId City ID
     */
    @Transactional
    public void approveEditedCity(Long editedCityId) {
        City edited = cityRepository.findById(editedCityId)
                .orElseThrow(() -> new RuntimeException("Edited city not found"));

        if (AttractionStatus.APPROVED.equals(edited.getStatus())) {
            throw new IllegalStateException("This city is already approved.");
        }
        AppUser adminUser = securityUtil.getCurrentUserId();

        if (edited.getOriginalCity() == null) {
            edited.setStatus(AttractionStatus.APPROVED);
            cityRepository.save(edited);
        } else {
            City original = edited.getOriginalCity();

            // Copy editable fields from edited to original
            original.setName(edited.getName());
            original.setLat(edited.getLat());
            original.setLng(edited.getLng());
            original.setCountry(edited.getCountry());
            original.setDescription(edited.getDescription());
            original.setLastModifiedAt(LocalDateTime.now());
            original.setApprovedBy(adminUser);

            cityRepository.delete(edited);
            cityRepository.save(original);
        }
    }

    /**
     * Reject edited City
     * @param editedCityId City ID
     */
    @Transactional
    public void rejectEditedCity(Long editedCityId) {
        City edited = cityRepository.findById(editedCityId)
                .orElseThrow(() -> new RuntimeException("Edited city not found"));

        if (edited.getOriginalCity() == null) {
            throw new IllegalStateException("Not an edited city.");
        }
        cityRepository.delete(edited);
    }

    /**
     * Delete City
     * @param editedCityId City ID
     */
    @Transactional
    public void deleteCity(Long editedCityId) {
        City edited = cityRepository.findById(editedCityId)
                .orElseThrow(() -> new RuntimeException("Edited city not found"));
        cityRepository.delete(edited);
    }

    /**
     * Delete City
     * @param city City Entity
     * @param dto City DTO
     */
    private void updateCityFields(City city, CityDTO dto) {
        city.setName(dto.getName());
        city.setLat(dto.getLat());
        city.setLng(dto.getLng());
        city.setCountry(dto.getCountry());
        city.setDescription(dto.getDescription());
    }
}