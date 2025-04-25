package com.tripdeio.backend.service;

import com.tripdeio.backend.dto.CityDTO;
import com.tripdeio.backend.entity.City;
import com.tripdeio.backend.repository.CityRepository;
import org.springframework.stereotype.Service;

@Service
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City createCity(CityDTO cityDTO) {
        City city = new City();
        city.setName(cityDTO.getName());
        city.setLat(cityDTO.getLat());
        city.setLng(cityDTO.getLng());
        city.setCountry(cityDTO.getCountry());
        city.setDescription(cityDTO.getDescription());
        return cityRepository.save(city);
    }
}