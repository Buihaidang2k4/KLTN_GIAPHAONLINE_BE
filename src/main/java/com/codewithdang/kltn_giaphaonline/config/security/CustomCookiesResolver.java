package com.codewithdang.kltn_giaphaonline.config.security;

import com.codewithdang.kltn_giaphaonline.utils.Constant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * find token from cookies
 */
@RequiredArgsConstructor
public class CookiesBearerTokenResolver implements BearerTokenResolver {

    private  final String cookieName;

    @Override
    public String resolve(HttpServletRequest request) {
        if (request.getCookies() == null) return null;


        return Arrays.stream(request.getCookies())
                .filter(c -> cookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .filter(StringUtils::hasText)
                .findFirst()
                .orElse(null);
    }
}
