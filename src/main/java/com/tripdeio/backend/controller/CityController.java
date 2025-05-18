package com.tripdeio.backend.controller;

import com.tripdeio.backend.dto.CityDTO;
import com.tripdeio.backend.entity.City;
import com.tripdeio.backend.repository.CityRepository;
import com.tripdeio.backend.service.AppUserService;
import com.tripdeio.backend.service.CityService;
import com.tripdeio.backend.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityService cityService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private SecurityUtil securityUtil;

    @PostMapping
    public ResponseEntity<City> createCity(@RequestBody CityDTO cityDTO) {
        City city = cityService.createCity(cityDTO);
        return ResponseEntity.ok(city);
    }

    @GetMapping("/all")
    public ResponseEntity<List<City>> getCities() {
        return ResponseEntity.ok(cityRepository.findAll());
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<City>> getCitiesByUser() {
        //return ResponseEntity.ok(cityRepository.findCitiesByUder());
        return null;
    }
}