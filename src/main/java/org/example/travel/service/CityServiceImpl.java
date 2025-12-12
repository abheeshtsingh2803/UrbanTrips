package org.example.travel.service;

import org.example.travel.domain.City;
import org.example.travel.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    public final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City createdCity(City city) {
        city.setId(null);
        return cityRepository.save(city);
    }

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public City findById(Long id) {
        return cityRepository.findById(id).orElseThrow(() -> new RuntimeException("City not found with id"));
    }

    @Override
    public City updatedCity(Long id, City city) {
        City existing = findById(id);
        existing.setName(city.getName());
        existing.setCountry(city.getCountry());
        existing.setDescription(city.getDescription());
        return cityRepository.save(existing);
    }

    @Override
    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
}
