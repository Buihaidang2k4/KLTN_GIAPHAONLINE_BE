package com.codewithdang.kltn_giaphaonline.config.cors;

import com.codewithdang.kltn_giaphaonline.config.fe.FrontendProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CorsConfig {

    FrontendProperties frontendProperties;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        List<String> allowedOrigins = new ArrayList<>();
        if (frontendProperties.getBaseUrl() != null && !frontendProperties.getBaseUrl().isBlank()) {
            allowedOrigins.add(frontendProperties.getBaseUrl().trim());
        }
        // Thêm fallback local dev nếu chưa có
        if (!allowedOrigins.contains("http://localhost:5173")) {
            allowedOrigins.add("http://localhost:5173");
        }

        corsConfiguration.setAllowedOrigins(allowedOrigins);
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setExposedHeaders(List.of("Set-Cookie", "Authorization"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
