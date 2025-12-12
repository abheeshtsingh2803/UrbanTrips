package org.example.travel.service;

import org.example.travel.domain.City;

import java.util.List;

public interface CityService {
    List<City> findAll();
    City findById(Long id);
    City createdCity(City city);
    City updatedCity(Long id, City city);
    void deleteCity(Long id);
}
