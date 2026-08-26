package com.codewithdang.kltn_giaphaonline.service.session;

import com.codewithdang.kltn_giaphaonline.dto.response.UserSessionRes;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SessionServiceImpl implements SessionService {

    RedisTemplate<String, Object> redisTemplate;

    private static final String SESSION_PREFIX = "user_session:";

    @Override
    public void saveSession(Long accountId, String jwtId, String email, String ipAddress, String userAgent, long ttlSeconds) {
        if (accountId == null || jwtId == null || ttlSeconds <= 0) return;

        String key = buildSessionKey(accountId, jwtId);

        UserSessionRes sessionData = UserSessionRes.builder()
                .jwtId(jwtId)
                .accountId(accountId)
                .email(email)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .loginAt(Instant.now())
                .isCurrentSession(true)
                .build();

        redisTemplate.opsForValue().set(key, sessionData, ttlSeconds, TimeUnit.SECONDS);
        log.info("Active session saved: accountId={}, jwtId={}, ttl={}s", accountId, jwtId, ttlSeconds);
    }

    @Override
    public void removeSession(Long accountId, String jwtId) {
        if (accountId == null || jwtId == null) return;
        String key = buildSessionKey(accountId, jwtId);
        redisTemplate.delete(key);
        log.info("Session removed: accountId={}, jwtId={}", accountId, jwtId);
    }

    @Override
    public void removeAllSessions(Long accountId) {
        if (accountId == null) return;
        String pattern = SESSION_PREFIX + accountId + ":*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("All active sessions revoked for accountId={}, count={}", accountId, keys.size());
        }
    }

    @Override
    public boolean isSessionActive(Long accountId, String jwtId) {
        if (accountId == null || jwtId == null) return false;
        String key = buildSessionKey(accountId, jwtId);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public List<UserSessionRes> getActiveSessions(Long accountId, String currentJwtId) {
        if (accountId == null) return Collections.emptyList();

        String pattern = SESSION_PREFIX + accountId + ":*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys == null || keys.isEmpty()) return Collections.emptyList();

        List<UserSessionRes> list = new ArrayList<>();
        for (String key : keys) {
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj instanceof UserSessionRes session) {
                session.setCurrentSession(session.getJwtId() != null && session.getJwtId().equals(currentJwtId));
                list.add(session);
            }
        }
        // Sắp xếp thời gian đăng nhập mới nhất lên đầu
        list.sort((a, b) -> {
            if (a.getLoginAt() == null || b.getLoginAt() == null) return 0;
            return b.getLoginAt().compareTo(a.getLoginAt());
        });
        return list;
    }

    private String buildSessionKey(Long accountId, String jwtId) {
        return SESSION_PREFIX + accountId + ":" + jwtId;
    }
}
