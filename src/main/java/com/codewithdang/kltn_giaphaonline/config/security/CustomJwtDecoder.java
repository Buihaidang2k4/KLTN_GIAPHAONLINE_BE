package com.codewithdang.kltn_giaphaonline.config.security;

import com.codewithdang.kltn_giaphaonline.service.revoked_token.RevokedTokenService;
import com.codewithdang.kltn_giaphaonline.service.session.SessionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.secret}")
    private String signingKey;

    private NimbusJwtDecoder jwtDecoder;
    private final RevokedTokenService revokedTokenService;
    private final SessionService sessionService;

    @PostConstruct
    public void init() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA256");
        this.jwtDecoder = NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            // 1. Kiểm tra Token Blacklist trong Redis / DB
            if (revokedTokenService.isTokenRevoked(token)) {
                log.warn("Access attempt with revoked token");
                throw new BadCredentialsException("Token has been revoked");
            }

            // 2. Decode và xác thực chữ ký + hạn token
            Jwt jwt = jwtDecoder.decode(token);

            // 3. Kiểm tra Active Session trong Redis 
            String jwtId = jwt.getId();
            Object accountIdClaim = jwt.getClaims().get("accountId");
            if (accountIdClaim != null && jwtId != null) {
                Long accountId = ((Number) accountIdClaim).longValue();
                if (!sessionService.isSessionActive(accountId, jwtId)) {
                    log.warn("Session is no longer active for accountId={}, jwtId={}", accountId, jwtId);
                    throw new BadCredentialsException("Session has been terminated or expired");
                }
            }

            return jwt;

        } catch (BadCredentialsException e) {
            log.warn("Authentication failed: {}", e.getMessage());
            throw new JwtException(e.getMessage(), e);
        } catch (JwtException e) {
            log.error("JWT decoding error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during token decoding: {}", e.getMessage(), e);
            throw new JwtException("Unexpected error: " + e.getMessage(), e);
        }
    }
}

