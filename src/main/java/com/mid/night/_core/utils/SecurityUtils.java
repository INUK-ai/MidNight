package com.mid.night._core.utils;

import com.mid.night._core.error.exception.Exception403;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getCurrentMemberId() {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if(name.equals("anonymousUser")) {
            throw new Exception403("익명의 유저는 접근 권한이 없습니다.");
        }

        return Long.parseLong(name);
    }
}
