package com.codewithdang.kltn_giaphaonline.service.revoked_token;


import java.time.Instant;

public interface RevokedTokenService {
    void revokedToken(String token, Instant expiresAt);

    boolean isTokenRevoked(String token);
}
