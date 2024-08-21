package com.mid.night.location.controller;

import com.mid.night._core.utils.ApiUtils;
import com.mid.night._core.utils.WebClientUtils;
import com.mid.night.location.dto.LocationRequestDTO;
import com.mid.night.location.dto.LocationResponseDTO;
import com.mid.night.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final WebClientUtils webClientUtils;

    private final LocationService locationService;

    private static final String REQUEST_URL = "http://192.168.1.43:7777/weather";

    /*
        위치 정보 가져오기
     */
    @PostMapping("/weather")
    public ResponseEntity<?> getWeatherToken() {

        LocationRequestDTO.GetLocationDTO getLocationDTO = locationService.getLocation();

        LocationResponseDTO.GetWeatherResultDTO responseDTO = webClientUtils.postSync(REQUEST_URL, getLocationDTO, LocationResponseDTO.GetWeatherResultDTO.class);

        // 토큰 생성
        log.info("결과값 : {}", responseDTO.result());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
