package com.mid.night.weather.service;

import com.mid.night.weather.dto.LocationRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LocationService {

    private static final int LATITUDE = 37;
    private static final int LONGITUDE = 37;

    public LocationRequestDTO.GetLocationDTO getLocation() {
        return new LocationRequestDTO.GetLocationDTO(LATITUDE, LONGITUDE);
    }
}
