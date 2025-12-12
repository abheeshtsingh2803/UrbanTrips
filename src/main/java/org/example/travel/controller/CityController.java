package org.example.travel.controller;

import org.example.travel.domain.City;
import org.example.travel.service.CityService;
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
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * GET /cities
     */
    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityService.findAll();
        return ResponseEntity.ok(cities);
    }

    /**
     * GET /cities/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<City> getCity(@PathVariable Long id) {
        try {
            City city = cityService.findById(id);
            return ResponseEntity.ok(city);
        } catch (RuntimeException ex) {
            // service throws RuntimeException when not found (adjust if your service uses a specific exception)
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /cities
     * Returns 201 Created with Location header pointing to the created resource
     */
    @PostMapping
    public ResponseEntity<City> createCity(@Validated  @RequestBody City city) {
        City created = cityService.createdCity(city);

        // build Location header: /cities/{id}
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>(created, headers, HttpStatus.CREATED);
    }

    /**
     * PUT /cities/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @Validated @RequestBody City city) {
        try {
            City updated = cityService.updatedCity(id, city);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /cities/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        try {
            cityService.deleteCity(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Generic fallback for unhandled exceptions to return HTTP 500 with a simple message.
     * (Optional) Remove or customize in favor of a global @ControllerAdvice if you prefer.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        // Log the exception in real app (logger.error(...))
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }
}
