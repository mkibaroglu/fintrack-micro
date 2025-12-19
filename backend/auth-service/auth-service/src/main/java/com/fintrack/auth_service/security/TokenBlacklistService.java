package com.fintrack.auth_service.security;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class TokenBlacklistService {

    private static final String KEY_PREFIX = "bl:jti:";

    private final StringRedisTemplate redis;

    public TokenBlacklistService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void blacklistJti(String jti, Instant tokenExpiresAt) {
        if (jti == null || jti.isBlank()) return;

        Duration ttl = Duration.between(Instant.now(), tokenExpiresAt);
        if (ttl.isNegative() || ttl.isZero()) return;

        String key = KEY_PREFIX + TokenHashUtil.sha256Hex(jti);
        redis.opsForValue().set(key, "1", ttl);
    }

    public boolean isJtiBlacklisted(String jti) {
        if (jti == null || jti.isBlank()) return false;
        String key = KEY_PREFIX + TokenHashUtil.sha256Hex(jti);
        Boolean exists = redis.hasKey(key);
        return Boolean.TRUE.equals(exists);
    }
}
