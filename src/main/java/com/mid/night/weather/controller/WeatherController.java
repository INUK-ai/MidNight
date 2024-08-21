package com.mid.night.weather.controller;

import com.mid.night._core.error.exception.Exception401;
import com.mid.night._core.utils.ApiUtils;
import com.mid.night._core.utils.WebClientUtils;
import com.mid.night.member.repository.MemberRepository;
import com.mid.night.weather.dto.LocationRequestDTO;
import com.mid.night.weather.dto.LocationResponseDTO;
import com.mid.night.weather.dto.WeatherTokenRequestDTO;
import com.mid.night.weather.service.LocationService;
import com.mid.night.weather.service.WeatherTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WebClientUtils webClientUtils;

    private final LocationService locationService;
    private final WeatherTokenService weatherTokenService;

    private static final String REQUEST_URL = "http://192.168.1.43:7777/weather";
    private static final String CurrentNickName = "MTVS";
    public static int GET_LOCATION_COUNT = 0;

    private final MemberRepository memberRepository;

    /*
        위치 정보 가져오기
     */
    @GetMapping("/location")
    public ResponseEntity<?> getWeatherToken() {

        log.info("get weather token");

        checkCurrentMember();

        LocationRequestDTO.GetLocationDTO getLocationDTO = locationService.getLocation(GET_LOCATION_COUNT);

        LocationResponseDTO.GetWeatherResultDTO resultDTO = webClientUtils.postSync(
                REQUEST_URL,
                getLocationDTO,
                LocationResponseDTO.GetWeatherResultDTO.class);

        // 토큰 생성
        log.info("결과값 : {}", resultDTO.Result());

        log.info("currentMemberId : " + CurrentNickName);

        GET_LOCATION_COUNT++;
        GET_LOCATION_COUNT %= 4;

        // 토큰 값 저장
        weatherTokenService.updateWeatherToken(CurrentNickName, resultDTO);

        return ResponseEntity.ok().body(ApiUtils.success(resultDTO));
    }

    private void checkCurrentMember() {

        memberRepository.findMemberByNickName(WeatherController.CurrentNickName)
                .orElseThrow(() -> new Exception401("허가되지 않은 사용자입니다."));
    }

    /*
        TODO: 날씨 토큰 값 업데이트
     */
    @PostMapping
    public ResponseEntity<?> useWeatherToken(@RequestBody WeatherTokenRequestDTO.UseWeatherTokenDTO useWeatherTokenDTO) {

        log.info("use weather token : " + useWeatherTokenDTO.Result());

        weatherTokenService.useWeatherToken(CurrentNickName, useWeatherTokenDTO);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
