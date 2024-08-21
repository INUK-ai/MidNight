package com.mid.night.weather.dto;

public class LocationRequestDTO {

    public record GetLocationDTO(
            int longitude,
            int latitude
    ) {
    }
}
