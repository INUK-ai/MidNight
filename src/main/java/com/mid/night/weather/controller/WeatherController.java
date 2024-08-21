package com.mid.night.weather.controller;

import com.mid.night._core.utils.ApiUtils;
import com.mid.night._core.utils.WebClientUtils;
import com.mid.night.weather.dto.LocationRequestDTO;
import com.mid.night.weather.dto.LocationResponseDTO;
import com.mid.night.weather.dto.WeatherTokenResponseDTO;
import com.mid.night.weather.service.LocationService;
import com.mid.night.weather.service.WeatherTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mid.night._core.utils.SecurityUtils.getCurrentMemberNickName;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WebClientUtils webClientUtils;

    private final LocationService locationService;
    private final WeatherTokenService weatherTokenService;

    private static final String REQUEST_URL = "http://192.168.1.43:7777/weather";

    /*
        위치 정보 가져오기
     */
    @GetMapping("/location")
    public ResponseEntity<?> getWeatherToken() {

        log.info("get weather token");

        LocationRequestDTO.GetLocationDTO getLocationDTO = locationService.getLocation();

        LocationResponseDTO.GetWeatherResultDTO resultDTO = webClientUtils.postSync(
                REQUEST_URL,
                getLocationDTO,
                LocationResponseDTO.GetWeatherResultDTO.class);

        // 토큰 생성
        log.info("결과값 : {}", resultDTO.result());

        // 토큰 값 저장
        weatherTokenService.updateWeatherToken(getCurrentMemberNickName(), resultDTO);

        return ResponseEntity.ok().body(ApiUtils.success(resultDTO));
    }

    /*
        TODO: 날씨 토큰 값 업데이트
     */
    @PostMapping
    public ResponseEntity<?> updateWeatherToken() {

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
