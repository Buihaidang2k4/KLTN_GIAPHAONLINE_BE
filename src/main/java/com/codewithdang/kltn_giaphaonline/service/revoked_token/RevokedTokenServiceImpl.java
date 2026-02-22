package com.codewithdang.kltn_giaphaonline.service.revoked_token;

import com.codewithdang.kltn_giaphaonline.entity.RevokedToken;
import com.codewithdang.kltn_giaphaonline.repo.RevokedTokenRepo;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RevokedTokenServiceImpl implements RevokedTokenService {
    RevokedTokenRepo revokedTokenRepo;
    RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_PREFIX = "revoked_token:";

    @Override
    public void revokedToken(String token, Instant expiresAt) {
        if (token == null || expiresAt == null) return;
        String jti = extractJti(token);

        String redisKey = REDIS_PREFIX + jti;

        Long ttlSeconds = expiresAt.getEpochSecond() - Instant.now().getEpochSecond();

        if (ttlSeconds <= 0) return;

        // save redis
        redisTemplate.opsForValue()
                .set(redisKey, "1", ttlSeconds, TimeUnit.SECONDS);

        revokedTokenRepo.save(
                RevokedToken.builder()
                        .token(jti)
                        .expiresAt(expiresAt)
                        .revokedAt(Instant.now())
                        .build()
        );
    }

    @Override
    public boolean isTokenRevoked(String token) {
        if (token == null) return true;

        String jti = extractJti(token);
        String redisKey = REDIS_PREFIX + jti;

        Boolean exists = redisTemplate.hasKey(redisKey);
        if (Boolean.TRUE.equals(exists)) return true;

        boolean revokedInDB = revokedTokenRepo.existsByToken(token);

        if (revokedInDB) {
            revokedTokenRepo.findByToken(token).ifPresent(rt -> {
                long ttlSeconds = rt.getExpiresAt().getEpochSecond() - Instant.now().getEpochSecond();
                if (ttlSeconds > 0) {
                    redisTemplate.opsForValue()
                            .set(redisKey, "1", ttlSeconds, java.util.concurrent.TimeUnit.SECONDS);
                }
            });
        }

        return revokedInDB;
    }

    private String extractJti(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getJWTID();
        } catch (Exception e) {
            log.error("Cannot get JTI from access token {}", e.getMessage());
            return null;
        }
    }
}
