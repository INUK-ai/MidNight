package com.mid.night._core.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JWTTokenFilter extends GenericFilterBean {

    private final JWTTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // Request Header 에서 JWT Token 추출
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        String requestURI = ((HttpServletRequest) servletRequest).getRequestURI();

        // 토큰 유효성 검사
        /*
            토큰 재발급 요청 시
            refresh token을 요청 headers에 Authorization 키로 받으면
            token이 존재하기 때문에
            getAuthentication 메소드가 동작
            -> 내부적으로 권한에 대한 정보가 없기 때문에 Exception 발생
            => 토큰 재발급 요청 path인 경우 필터 패스
         */
        if(token != null && jwtTokenProvider.validateToken(token)) {
            if(!requestURI.equals("/api/auth/reissue") && !requestURI.equals("/api/auth/logout")) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info(authentication.getName() + " 님이 로그인 하였습니다.");
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
