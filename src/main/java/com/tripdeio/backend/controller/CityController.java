package com.tripdeio.backend.controller;

import com.tripdeio.backend.dto.CityDTO;
import com.tripdeio.backend.entity.City;
import com.tripdeio.backend.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping
    public ResponseEntity<CityDTO> createCity(@RequestBody CityDTO cityDTO) {
        City city = cityService.createCity(cityDTO);
        return ResponseEntity.ok(new CityDTO(city));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CityDTO>> getCities() {
        return ResponseEntity.ok(cityService.findAll());
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<CityDTO>> getCitiesByUser() {
        return ResponseEntity.ok(cityService.findByUserId());
    }

    /**
     * Get All Approved Cities
     * @return list of Cities
     */
    @GetMapping("/all-approved")
    public ResponseEntity<List<CityDTO>> getApprovedCities() {
        return ResponseEntity.ok(cityService.findApprovedCities());
    }

    /**
     * Get All Pending Cities
     * @return list of Cities
     */
    @GetMapping("/all-pending")
    public ResponseEntity<List<CityDTO>> getPendingCities() {
        return ResponseEntity.ok(cityService.findPendingCities());
    }

    /**
     * Update City
     * @param cityDTO DTO from UI
     * @return list of Cities
     */
    @PutMapping("/update")
    public ResponseEntity<CityDTO> updateCity(@RequestBody CityDTO cityDTO) {
        City city = cityService.updateCity(cityDTO);
        return ResponseEntity.ok(new CityDTO(city));
    }

    /**
     * Approve City Update
     * @return Response
     */
    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveCity(@PathVariable Long id) {
        cityService.approveEditedCity(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Reject City Update
     * @return Response
     */
    @PutMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectCity(@PathVariable Long id) {
        cityService.rejectEditedCity(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete City
     * @return Response
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.ok().build();
    }
}