package com.mid.night.redis.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refresh", timeToLive = 259200) // 60 * 60 * 24 * 3
public class RefreshToken {

    @Id
    private String id;
    private Collection<? extends GrantedAuthority> authorities;
    @Indexed
    private String refreshToken;

    @Builder
    public RefreshToken(String id, Collection<? extends GrantedAuthority> authorities, String refreshToken) {
        this.id = id;
        this.authorities = authorities;
        this.refreshToken = refreshToken;
    }
}
