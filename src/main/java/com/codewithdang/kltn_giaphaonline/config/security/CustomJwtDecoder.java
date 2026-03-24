package com.codewithdang.kltn_giaphaonline.config.security;

import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.service.auth.AuthService;
import com.nimbusds.jose.JOSEException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;

// anayless
@Component
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {

    @Autowired
    private AuthService authService;

    @Value("${jwt.secret}")
    private String signingKey;

    private NimbusJwtDecoder jwtDecoder;

    @PostConstruct
    public void init() {
        // init secret key
        SecretKeySpec secretKeySpec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA256");
        this.jwtDecoder = NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
//            var res = authService.introspect(token);

//            if (!res.isValid())
//                throw new AppException(ErrorCode.TOKEN_INVALID);
            log.info("JWT Token: {}", token);
            return jwtDecoder.decode(token);

        } catch (JwtException e) {
            log.error("JWT decoding error: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during token decoding: {}", e.getMessage(), e);
            throw new JwtException("Unexpected error: " + e.getMessage());
        }
    }

}

