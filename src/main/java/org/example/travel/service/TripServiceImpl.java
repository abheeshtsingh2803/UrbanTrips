package org.example.travel.service;

import org.example.travel.domain.City;
import org.example.travel.domain.Trip;
import org.example.travel.dto.TripRequest;
import org.example.travel.dto.TripResponse;
import org.example.travel.repository.CityRepository;
import org.example.travel.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final CityRepository cityRepository;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository, CityRepository cityRepository) {
        this.tripRepository = tripRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public List<TripResponse> findAll() {
        return tripRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TripResponse findById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found with id " + id));
        return toResponse(trip);
    }

    @Override
    @Transactional
    public TripResponse createTrip(TripRequest request) {
        // 1. load the managed City entity (throws if not found)
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found with id " + request.getCityId()));

        // 2. create Trip entity and set the managed city
        Trip trip = new Trip();
        trip.setCity(city);
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setRating(request.getRating());
        trip.setPersonalNotes(request.getPersonalNotes());

        // 3. save Trip
        Trip saved = tripRepository.save(trip);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public TripResponse upadteTrip(Long id, TripRequest request) {
        Trip existing = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found with id " + id));

        // fetch city (ensure managed)
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found with id " + request.getCityId()));

        existing.setCity(city);
        existing.setStartDate(request.getStartDate());
        existing.setEndDate(request.getEndDate());
        existing.setRating(request.getRating());
        existing.setPersonalNotes(request.getPersonalNotes());

        Trip updated = tripRepository.save(existing);
        return toResponse(updated);
    }

    @Override
    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }

    // helper to build DTO
    private TripResponse toResponse(Trip trip) {
        TripResponse r = new TripResponse();
        r.setId(trip.getId());
        r.setCityId(trip.getCity().getId());
        r.setCityName(trip.getCity().getName());
        r.setCountry(trip.getCity().getCountry());
        r.setStartDate(trip.getStartDate());
        r.setEndDate(trip.getEndDate());
        r.setRating(trip.getRating());
        r.setPersonalNotes(trip.getPersonalNotes());
        return r;
    }
}
