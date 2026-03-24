package com.codewithdang.kltn_giaphaonline.config.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * rate limit anti spam connect
 */
@Component
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {
    // Stored using separate IP addresses.
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Value("${app.rate-limit.capacity}")
    private int capacity;

    @Value("${app.rate-limit.duration-minutes}")
    private int durationMinutes;


    private Bucket createNewBucket(String key) {
        log.info("Initialize new bandwidth limits for the IP address: {}", key);
        return Bucket.builder()
                .addLimit(Bandwidth.classic(capacity
                        , Refill.intervally(capacity, Duration.ofMinutes(durationMinutes))
                ))
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // get ip
        String ip = getClientIP(request);

        // get bucket, if not found add new
        Bucket bucket = buckets.computeIfAbsent(ip, this::createNewBucket);

        if(bucket.tryConsume(1)) filterChain.doFilter(request, response);
        else handleLimitExceeded(response);

    }


    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            xfHeader = request.getRemoteAddr();
        }

        return xfHeader.split(",")[0]; // get ip
    }


    private void handleLimitExceeded(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write("""
                {
                    "code": 429,
                    "message": "Bạn đã gửi quá nhiều yêu cầu. Vui lòng thử lại sau ít phút.",
                    "status": "TOO_MANY_REQUESTS"
                }
                """);
    }


}
