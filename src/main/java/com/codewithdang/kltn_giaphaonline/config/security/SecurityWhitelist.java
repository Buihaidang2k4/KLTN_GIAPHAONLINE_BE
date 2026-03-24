package com.codewithdang.kltn_giaphaonline.config.security;

import java.util.List;

public final class SecurityWhitelist {
    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            // Auth
            "/api/v1/auth/**"
    );
    // swagger
    public static final List<String> SWAGGER_ENDPOINTS = List.of(
            "/swagger-ui/**", "/v3/api-docs/**",
            "/swagger-resources/**", "/webjars/**"
    );


    private SecurityWhitelist() {
    }
}
