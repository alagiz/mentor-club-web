package com.mentor.club.model.authentication.token;

import lombok.Getter;

public enum JwtTokenLifetime {
    PASSWORD_RESET_TOKEN_LIFESPAN_IN_SECONDS(24L * 3600L),    // 24 hours
    REFRESH_TOKEN_LIFESPAN_IN_SECONDS(30L * 24L * 3600L),     // 30 days
    ACCESS_TOKEN_LIFESPAN_IN_SECONDS(10L * 60L);              // 10 minutes

    @Getter
    private final Long lifetime;

    JwtTokenLifetime(Long lifetime) {
        this.lifetime = lifetime;
    }
}