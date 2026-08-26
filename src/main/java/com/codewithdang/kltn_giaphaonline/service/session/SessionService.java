package com.codewithdang.kltn_giaphaonline.service.session;

import com.codewithdang.kltn_giaphaonline.dto.response.UserSessionRes;

import java.util.List;

public interface SessionService {

    void saveSession(Long accountId, String jwtId, String email, String ipAddress, String userAgent, long ttlSeconds);

    void removeSession(Long accountId, String jwtId);

    void removeAllSessions(Long accountId);

    boolean isSessionActive(Long accountId, String jwtId);

    List<UserSessionRes> getActiveSessions(Long accountId, String currentJwtId);
}
