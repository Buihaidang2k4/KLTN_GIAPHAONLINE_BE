package com.codewithdang.kltn_giaphaonline.config.security;

import com.codewithdang.kltn_giaphaonline.utils.SecurityMatcherUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * find token from cookies, check request before filter
 */
@Slf4j
@RequiredArgsConstructor
public class CustomCookiesResolver implements BearerTokenResolver {

    private final String cookieName;

    @Override
    public String resolve(HttpServletRequest request) {
        String path = request.getRequestURI();

        if (request.getCookies() == null) return null;
        if (SecurityMatcherUtils.isPublicOrSwagger(path)) return null;

        return Arrays.stream(request.getCookies())
                .filter(c -> cookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .filter(StringUtils::hasText)
                .findFirst()
                .orElse(null);
    }
}
