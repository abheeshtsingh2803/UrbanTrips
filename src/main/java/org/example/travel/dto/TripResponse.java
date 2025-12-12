package org.example.travel.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TripResponse {
    private Long id;
    private Long cityId;
    private String cityName;
    private String country;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer rating;
    private String personalNotes;
}
