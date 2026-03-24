package com.codewithdang.kltn_giaphaonline.service.revoked_token;

import com.codewithdang.kltn_giaphaonline.entity.RevokedToken;
import com.codewithdang.kltn_giaphaonline.repo.RevokedTokenRepo;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
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
    public void revokedToken(String token, Instant expiresAt, HttpServletRequest request) {
        if (token == null || token.isBlank() || expiresAt == null) return;


        long ttlSeconds = expiresAt.getEpochSecond() - Instant.now().getEpochSecond();
        if (ttlSeconds <= 0) return;

        String tokenHash = hashToken(token);
        String redisKey = REDIS_PREFIX + tokenHash;
        String ipAddress = getClientIp(request);
        String deviceInfo = request.getHeader("User-Agent");

        // save redis
        redisTemplate.opsForValue().set(redisKey, "1", ttlSeconds, TimeUnit.SECONDS);

        revokedTokenRepo.save(
                RevokedToken.builder()
                        .tokenHash(tokenHash)
                        .revokedAt(Instant.now())
                        .ipAddress(ipAddress)
                        .deviceInfo(deviceInfo)
                        .expiresAt(expiresAt)
                        .build()
        );
    }

    @Override
    public boolean isTokenRevoked(String token) {
        if (token == null || token.isBlank()) return true;

        String tokenHash = hashToken(token);
        String redisKey = REDIS_PREFIX + tokenHash;

        Boolean exists = redisTemplate.hasKey(redisKey);
        if (Boolean.TRUE.equals(exists)) return true;

        boolean revokedInDB = revokedTokenRepo.existsByTokenHash(token);

        if (revokedInDB) {
            revokedTokenRepo.findByTokenHash(token).ifPresent(rt -> {
                long ttlSeconds = rt.getExpiresAt().getEpochSecond() - Instant.now().getEpochSecond();
                if (ttlSeconds > 0) {
                    redisTemplate.opsForValue()
                            .set(redisKey, "1", ttlSeconds, java.util.concurrent.TimeUnit.SECONDS);
                }
            });
        }

        return revokedInDB;
    }

    private String hashToken(String token) {
        try {
            // Khoi tao thuan toan
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Hashing
            byte[] hashBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (Exception e) {
            log.error("Cannot hash token", e);
            throw new IllegalStateException("Cannot hash token", e);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        // lưu trữ ip thật nếu  nếu proxy chuyển tiếp yêu cầu thì sẽ có (nginx, loadBalancer)
        String remoteAddr = request.getHeader("X-Forwarded-For");
        if (remoteAddr == null || remoteAddr.isEmpty()) {
            remoteAddr = request.getRemoteAddr();
        } else {
            // X-Forwarded-For có thể chứa chuỗi IP, lấy cái đầu tiên
            remoteAddr = remoteAddr.split(",")[0];
        }
        return remoteAddr;
    }


}
