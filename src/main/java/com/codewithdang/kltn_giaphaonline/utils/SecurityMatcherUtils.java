package com.codewithdang.kltn_giaphaonline.utils;

import com.codewithdang.kltn_giaphaonline.config.security.SecurityWhitelist;
import org.springframework.util.AntPathMatcher;

public class SecurtityMatcherUtils {
    private static final AntPathMatcher matcher = new AntPathMatcher();

    public static boolean isPublic(String path) {
        return SecurityWhitelist.PUBLIC_ENDPOINTS.stream()
                .anyMatch(p ->  matcher.match(p, path));
    }

}
