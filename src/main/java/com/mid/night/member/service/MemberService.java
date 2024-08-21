package com.mid.night.member.service;

import com.mid.night._core.error.exception.Exception400;
import com.mid.night._core.jwt.JWTTokenProvider;
import com.mid.night.member.domain.Authority;
import com.mid.night.member.domain.Member;
import com.mid.night.member.dto.MemberRequestDTO;
import com.mid.night.member.dto.MemberResponseDTO;
import com.mid.night.member.repository.MemberRepository;
import com.mid.night.plant.domain.Plant;
import com.mid.night.plant.repository.PlantRepository;
import com.mid.night.redis.domain.RefreshToken;
import com.mid.night.redis.repository.RefreshTokenRedisRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PlantRepository plantRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JWTTokenProvider jwtTokenProvider;

    @Transactional
    public MemberResponseDTO.authTokenDTO login(HttpServletRequest httpServletRequest, MemberRequestDTO.loginDTO requestDTO) {
        String nickName = requestDTO.UserName();

        // 1. 닉네임 확인 및 회원 생성
        findMemberByNickName(nickName)
                .ifPresentOrElse(
                        member -> log.info("기존 회원 로그인: {}", nickName),
                        () -> newMember(nickName)
                );

        Member member = memberRepository.findMemberByNickName(nickName)
                .orElseThrow(() -> new Exception400("해당 닉네임의 회원을 찾을 수 없습니다."));

        initWeatherToken(member);

        // 1. 닉네임 확인 및 회원 생성
        findPlantByMember(member)
                .ifPresentOrElse(
                        plant -> log.info("기존 회원 로그인: {}", plant.getPlantName()),
                        () -> newPlant(member)
                );

        Plant plant = plantRepository.findPlantByMember(member)
                .orElseThrow(() -> new Exception400("해당 회원은 키우고 있는 식물이 없습니다."));

        return getAuthTokenDTO(member, plant, httpServletRequest);
    }

    private void initWeatherToken(Member member) {
        member.initSunny(2);
        member.initCloudy(2);
        member.initWindy(2);
        member.initRainy(2);
        member.initSnowy(2);
        member.initPlantNums(0);

        memberRepository.save(member);
    }

    private Optional<Plant> findPlantByMember(Member member) {
        log.info("식물 확인");

        return plantRepository.findPlantByMember(member);
    }

    protected Optional<Member> findMemberByNickName(String nickName) {
        log.info("회원 확인 : {}", nickName);

        return memberRepository.findMemberByNickName(nickName);
    }

    // 회원 생성
    protected void newMember(String nickName) {
        Member newMember = Member.builder()
                .nickName(nickName)
                .password(passwordEncoder.encode(nickName))
                .authority(Authority.USER)
                .build();

        memberRepository.save(newMember);
        log.info("새로운 회원 생성: " + nickName);
    }

    // 식물 생성
    protected void newPlant(Member member) {
        Plant newPlant = Plant.builder()
                .member(member)
                .build();

        plantRepository.save(newPlant);
        log.info("새로운 회원 생성: " + newPlant.getPlantName());
    }

    // 토큰 발급
    protected MemberResponseDTO.authTokenDTO getAuthTokenDTO(Member member, Plant plant, HttpServletRequest httpServletRequest) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(member.getNickName(), member.getNickName());
        AuthenticationManager manager = authenticationManagerBuilder.getObject();
        Authentication authentication = manager.authenticate(usernamePasswordAuthenticationToken);

        // 권한 정보를 추출하여 Collection<? extends GrantedAuthority>로 변환
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        MemberResponseDTO.authTokenDTO authTokenDTO = jwtTokenProvider.generateToken(
                member,
                plant,
                authorities
        );

        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(authentication.getName())
                .authorities(authentication.getAuthorities())
                .refreshToken(authTokenDTO.refreshToken())
                .build()
        );

        return authTokenDTO;
    }

    /*
        로그아웃
     */
    @Transactional
    public MemberResponseDTO.RecordDTO logout(HttpServletRequest httpServletRequest, String currentMemberNickName) {

        log.info("로그아웃 - Refresh Token 확인");

        return getRecordData(currentMemberNickName);
    }

    private MemberResponseDTO.RecordDTO getRecordData(String currentMemberNickName) {

        Member member = memberRepository.findMemberByNickName(currentMemberNickName)
                .orElseThrow(() -> new Exception400("해당 닉네임의 회원을 찾을 수 없습니다."));

        Plant plant = plantRepository.findPlantByMember(member)
                .orElseThrow(() -> new Exception400("해당 회원은 키우고 있는 식물이 없습니다."));

        member.updatePlantNums();

        return new MemberResponseDTO.RecordDTO(
                member.getNickName(),
                String.valueOf(member.getPlantNums()),
                plant.getPlantName(),
                String.valueOf(plant.getGrowthGauge())
        );
    }
}
