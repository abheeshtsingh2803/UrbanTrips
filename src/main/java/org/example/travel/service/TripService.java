package org.example.travel.service;

import org.example.travel.dto.TripRequest;
import org.example.travel.dto.TripResponse;

import java.util.List;

public interface TripService {
    List<TripResponse> findAll();
    TripResponse findById(Long id);
    TripResponse createTrip(TripRequest request);
    TripResponse upadteTrip(Long id, TripRequest request);
    void deleteTrip(Long id);
}
