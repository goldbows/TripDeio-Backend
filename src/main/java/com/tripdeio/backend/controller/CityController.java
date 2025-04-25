package com.tripdeio.backend.controller;

import com.tripdeio.backend.dto.CityDTO;
import com.tripdeio.backend.entity.City;
import com.tripdeio.backend.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cities")
public class CityController {
    @Autowired
    private CityService cityService;

    @PostMapping
    public ResponseEntity<City> createCity(@RequestBody CityDTO cityDTO) {
        City city = cityService.createCity(cityDTO);
        return ResponseEntity.ok(city);
    }
}