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

    // 맑음, 구름, 비
    private static final int[] LATITUDE = {38, 78, 27, 38};
    private static final int[] LONGITUDE = {38, 78, 76, 38};

    public LocationRequestDTO.GetLocationDTO getLocation(int getLocationCount) {

        log.info("getLocationCount: {}", getLocationCount);

        log.info("latitude : {}, longitude : {}", LATITUDE[getLocationCount], LONGITUDE[getLocationCount]);

        return new LocationRequestDTO.GetLocationDTO(LATITUDE[getLocationCount], LONGITUDE[getLocationCount]);
    }
}
