package org.example.travel.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TripRequest {
    private Long CityId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int rating;
    private String personalNotes;
}
