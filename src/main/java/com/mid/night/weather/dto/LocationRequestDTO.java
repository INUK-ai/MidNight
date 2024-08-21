package com.mid.night.weather.dto;

public class LocationRequestDTO {

    public record GetLocationDTO(
            int latitude,
            int longitude
    ) {
    }
}
