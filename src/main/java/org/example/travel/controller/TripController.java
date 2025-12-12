package org.example.travel.controller;

import org.example.travel.dto.TripRequest;
import org.example.travel.dto.TripResponse;
import org.example.travel.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    /**
     * GET /trips
     */
    @GetMapping
    public ResponseEntity<List<TripResponse>> getAllTrips() {
        List<TripResponse> trips = tripService.findAll();
        return ResponseEntity.ok(trips);
    }

    /**
     * GET /trips/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TripResponse> getTrip(@PathVariable Long id) {
        try {
            TripResponse trip = tripService.findById(id);
            return ResponseEntity.ok(trip);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /trips
     * Returns 201 Created with Location header pointing to the created resource
     */
    @PostMapping
    public ResponseEntity<TripResponse> createTrip(@Validated @RequestBody TripRequest request) {
        TripResponse created = tripService.createTrip(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>(created, headers, HttpStatus.CREATED);
    }

    /**
     * PUT /trips/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TripResponse> updateTrip(@PathVariable Long id,
                                                   @Validated @RequestBody TripRequest request) {
        try {
            TripResponse updated = tripService.upadteTrip(id, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /trips/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        try {
            tripService.deleteTrip(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Fallback for unexpected exceptions — returns 500 with a simple message.
     * For larger apps replace with a global @ControllerAdvice.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }
}
