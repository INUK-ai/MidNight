package com.mid.night.location.dto;

public class LocationRequestDTO {

    public record GetLocationDTO(
            int longitude,
            int latitude
    ) {
    }
}
