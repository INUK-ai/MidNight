package com.mid.night.weather.service;


import com.mid.night._core.error.exception.Exception400;
import com.mid.night.member.domain.Member;
import com.mid.night.member.repository.MemberRepository;
import com.mid.night.weather.dto.LocationResponseDTO;
import com.mid.night.weather.dto.WeatherTokenRequestDTO;
import com.mid.night.weather.dto.WeatherTokenResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class WeatherTokenService {

    private final MemberRepository memberRepository;

    @Transactional
    public void updateWeatherToken(String currentMemberNickName, LocationResponseDTO.GetWeatherResultDTO resultDTO) {

        Member member = memberRepository.findMemberByNickName(currentMemberNickName)
                .orElseThrow(() -> new Exception400("해당 닉네임의 회원을 찾을 수 없습니다."));

        String weatherResult = resultDTO.Result();

        if (weatherResult == null) {
            log.error("Weather result is null for member: {}", currentMemberNickName);
            throw new Exception400("Weather result cannot be null");
        }

        switch (weatherResult) {
            case "Sunny":
                member.updateSunny(1);
                break;
            case "Cloudy":
                member.updateCloudy(1);
                break;
            case "Windy":
                member.updateWindy(1);
                break;
            case "Rainy":
                member.updateRainy(1);
                break;
            case "Snowy":
                member.updateSnowy(1);
                break;
            default:
                log.error("Unexpected weather result: {}", weatherResult);
                throw new Exception400("Unexpected weather result: " + weatherResult);
        }
    }

    public void useWeatherToken(String currentMemberNickName, WeatherTokenRequestDTO.UseWeatherTokenDTO useWeatherTokenDTO) {

        Member member = memberRepository.findMemberByNickName(currentMemberNickName)
                .orElseThrow(() -> new Exception400("해당 닉네임의 회원을 찾을 수 없습니다."));

        String weatherToken = useWeatherTokenDTO.Result();

        if (weatherToken == null) {
            log.error("Weather token is null for member: {}", currentMemberNickName);
            throw new Exception400("Weather result cannot be null");
        }

        switch (weatherToken) {
            case "Sunny":
                if(member.getSunny() < 1) {
                    throw new Exception400("사용 가능한 " + weatherToken + " 이 없습니다.");
                }
                member.updateSunny(-1);
                break;
            case "Cloudy":
                if(member.getCloudy() < 1) {
                    throw new Exception400("사용 가능한 " + weatherToken + " 이 없습니다.");
                }
                member.updateCloudy(-1);
                break;
            case "Windy":
                if(member.getWindy() < 1) {
                    throw new Exception400("사용 가능한 " + weatherToken + " 이 없습니다.");
                }
                member.updateWindy(-1);
                break;
            case "Rainy":
                if(member.getRainy() < 1) {
                    throw new Exception400("사용 가능한 " + weatherToken + " 이 없습니다.");
                }
                member.updateRainy(-1);
                break;
            case "Snowy":
                if(member.getSnowy() < 1) {
                    throw new Exception400("사용 가능한 " + weatherToken + " 이 없습니다.");
                }
                member.updateSnowy(-1);
                break;
            default:
                log.error("Unexpected weather : {}", weatherToken);
                throw new Exception400("Unexpected weather result: " + weatherToken);
        }
    }
}
