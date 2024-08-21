package com.mid.night.member.controller;

import com.mid.night._core.utils.ApiUtils;
import com.mid.night.member.dto.MemberRequestDTO;
import com.mid.night.member.dto.MemberResponseDTO;
import com.mid.night.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class MemberController {

    private final MemberService memberService;

    private static final String CurrentNickName = "MTVS";

    /*
        기본 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest httpServletRequest, @Valid @RequestBody MemberRequestDTO.loginDTO requestDTO) {

        MemberResponseDTO.authTokenDTO responseDTO = memberService.login(httpServletRequest, requestDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {

        log.info("로그아웃 시도");

        MemberResponseDTO.RecordDTO responseDTO = memberService.logout(httpServletRequest, CurrentNickName);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}