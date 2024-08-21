package com.mid.night.member.service;

import com.mid.night._core.error.exception.Exception400;
import com.mid.night._core.jwt.JWTTokenProvider;
import com.mid.night.member.domain.Authority;
import com.mid.night.member.domain.Member;
import com.mid.night.member.dto.MemberRequestDTO;
import com.mid.night.member.dto.MemberResponseDTO;
import com.mid.night.member.repository.MemberRepository;
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
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JWTTokenProvider jwtTokenProvider;

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

        return getAuthTokenDTO(member, httpServletRequest);
    }

    // 비밀번호 확인
    private void checkValidPassword(String rawPassword, String encodedPassword) {

        log.info("{} {}", rawPassword, encodedPassword);

        if(!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new Exception400("비밀번호가 일치하지 않습니다.");
        }
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

    // 토큰 발급
    protected MemberResponseDTO.authTokenDTO getAuthTokenDTO(Member member, HttpServletRequest httpServletRequest) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(member.getNickName(), member.getNickName());
        AuthenticationManager manager = authenticationManagerBuilder.getObject();
        Authentication authentication = manager.authenticate(usernamePasswordAuthenticationToken);

        // 권한 정보를 추출하여 Collection<? extends GrantedAuthority>로 변환
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        MemberResponseDTO.authTokenDTO authTokenDTO = jwtTokenProvider.generateToken(
                member,
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
    public MemberResponseDTO.RecordDTO logout(HttpServletRequest httpServletRequest, String currentMemberNickName) {

        log.info("로그아웃 - Refresh Token 확인");

        String token = jwtTokenProvider.resolveToken(httpServletRequest);

        if(token == null || !jwtTokenProvider.validateToken(token)) {
            throw new Exception400("유효하지 않은 Refresh Token 입니다.");
        }

        RefreshToken refreshToken = refreshTokenRedisRepository.findByRefreshToken(token);
        refreshTokenRedisRepository.delete(refreshToken);

        return getRecordData(currentMemberNickName);
    }

    private MemberResponseDTO.RecordDTO getRecordData(String nickName) {

        Member member = memberRepository.findMemberByNickName(nickName)
                .orElseThrow(() -> new Exception400("해당 닉네임의 회원을 찾을 수 없습니다."));

        String plantName = "토마토";

        return new MemberResponseDTO.RecordDTO(member.getNickName(), null, plantName);
    }
}
