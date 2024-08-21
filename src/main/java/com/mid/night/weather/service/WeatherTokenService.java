package com.mid.night.weather.service;


import com.mid.night._core.error.exception.Exception400;
import com.mid.night.member.domain.Member;
import com.mid.night.member.repository.MemberRepository;
import com.mid.night.weather.dto.LocationResponseDTO;
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

        switch (resultDTO.Result()) {
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
                log.error("Unexpected weather result: {}", resultDTO.Result());
                throw new Exception400("Unexpected weather result: " + resultDTO.Result());
        }

        // 혹시 모르니까
        memberRepository.save(member);
    }
}
