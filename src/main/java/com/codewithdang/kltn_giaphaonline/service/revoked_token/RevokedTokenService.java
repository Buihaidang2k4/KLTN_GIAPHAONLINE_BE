package com.codewithdang.kltn_giaphaonline.service.revoked_token;


import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;

public interface RevokedTokenService {
    void revokedToken(String token, Instant expiresAt, HttpServletRequest request);

    boolean isTokenRevoked(String token);
}
