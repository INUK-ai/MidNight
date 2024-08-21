package com.mid.night.member.dto;

import jakarta.validation.constraints.NotBlank;

public class MemberRequestDTO {

    // 기본 로그인
    public record loginDTO(
            @NotBlank(message = "닉네임을 입력해 주세요.")
            String UserName
    ) {
    }
}
