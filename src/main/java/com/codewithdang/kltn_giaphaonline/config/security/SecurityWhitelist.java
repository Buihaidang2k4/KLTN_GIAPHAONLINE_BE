package com.codewithdang.kltn_giaphaonline.config.security;

import java.util.List;

public final class SecurityWhitelist {
    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            // Auth
            "/api/v1/auth/**",
            "/api/v1/auth/login",
            "/api/v1/auth/register-by-invitation",
            "/api/v1/auth/register",
            "/api/v1/auth/refresh-token",
            "/api/v1/auth/login",
            "/api/v1/auth/introspect",
            "/api/v1/auth/verify-account/**",
            "/api/v1/auth/re-send-token-verify/**",
            // Payment callbacks
            "/api/v1/payments/vnpay/callback",
            "/api/v1/subscription-plans",
            "/api/v1/article-categories",
            "/api/v1/articles",
            "/api/v1/articles/slug/**"

    );
    // swagger
    public static final List<String> SWAGGER_ENDPOINTS = List.of(
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/configuration/ui",
            "/configuration/security"
    );


    private SecurityWhitelist() {
    }
}
