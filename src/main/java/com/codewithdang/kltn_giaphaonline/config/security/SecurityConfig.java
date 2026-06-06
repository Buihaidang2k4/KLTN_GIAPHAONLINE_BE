package com.codewithdang.kltn_giaphaonline.config.security;

import com.codewithdang.kltn_giaphaonline.enums.RoleEnums;
import com.codewithdang.kltn_giaphaonline.utils.ConstantUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.codewithdang.kltn_giaphaonline.config.security.SecurityWhitelist.SWAGGER_ENDPOINTS;

/**
 * Allow or block requests
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SecurityConfig {

    @Autowired
    CustomJwtDecoder customJwtDecoder;
    @Autowired
    JwtAuthEntryPoint jwtAuthEntryPoint;
    @Autowired
    RateLimitFilter rateLimitFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(httpBasic -> httpBasic.disable())

                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(SecurityWhitelist.PUBLIC_ENDPOINTS.toArray(String[]::new)).permitAll()
                                .requestMatchers(SWAGGER_ENDPOINTS.toArray(String[]::new)).permitAll()
                                .anyRequest().authenticated()
                )

                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)

                .oauth2ResourceServer(oauth ->
                        oauth.bearerTokenResolver(new CustomCookiesResolver(ConstantUtils.ACCESS_TOKEN))
                                .jwt(jwt ->
                                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                                                .decoder(customJwtDecoder)
                                )
                                .authenticationEntryPoint(jwtAuthEntryPoint)

                );

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // Cho phép Frontend của bạn truy cập
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // 1. Sử dụng converter mặc định để lấy Roles từ claim "scope"
            JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();
            defaultConverter.setAuthorityPrefix("");

            Collection<GrantedAuthority> authorities = defaultConverter.convert(jwt);

            // 2. Tạo một tập hợp mới để chứa cả Role và Permission
            Set<GrantedAuthority> totalAuthorities = new HashSet<>(authorities);

            // 3. Duyệt qua từng Role để tìm Permission
            for (GrantedAuthority auth : authorities) {
                String roleName = auth.getAuthority();
                // Xóa prefix "ROLE_" nếu có để khớp với tên trong Enum
                String cleanRoleName = roleName.startsWith("ROLE_")
                        ? roleName.substring(5)
                        : roleName;

                try {
                    RoleEnums roleEnum = RoleEnums.valueOf(cleanRoleName);

                    // Add tất cả Permission của Role đó vào SecurityContext
                    roleEnum.getPermissionEnums().forEach(permission -> {
                        totalAuthorities.add(new SimpleGrantedAuthority(permission.name()));
                    });

//                    log.info("Mapped Role [{}] to {} permissions", cleanRoleName, roleEnum.getPermissionEnums().size());
                } catch (IllegalArgumentException e) {
                    log.warn("Role [{}] not found in RoleEnums, skipping permission mapping", cleanRoleName);
                }
            }

            return totalAuthorities;
        });

        return converter;
    }

}
