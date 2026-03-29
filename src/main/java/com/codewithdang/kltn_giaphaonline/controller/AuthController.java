package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.AuthReq;
import com.codewithdang.kltn_giaphaonline.dto.request.RegisterReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.AuthRes;
import com.codewithdang.kltn_giaphaonline.service.account_verification_token.AccountVerificationTokenService;
import com.codewithdang.kltn_giaphaonline.service.auth.AuthService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Auth Management")
public class AuthController {
    AuthService authService;
    AccountVerificationTokenService verificationTokenService;

    @PostMapping("/login")
    ResponseEntity<ApiResponse<AuthRes>> login(@Valid @RequestBody AuthReq authReq,
                                               HttpServletResponse response
    ) throws ParseException {
        return ResponseEntity.ok(new ApiResponse<>(200, "LOGIN SUCCESS", authService.authenticate(authReq, response)));
    }

    @PostMapping("/register")
    ResponseEntity<ApiResponse<AuthRes>> register(@Valid @RequestBody RegisterReq registerReq, HttpServletRequest request) {
        String requestIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        authService.register(registerReq, requestIp, userAgent);
        return ResponseEntity.ok(new ApiResponse<>(200, "Register success", null));
    }

    @PostMapping("/refresh-token")
    ResponseEntity<ApiResponse<?>> refreshToken(HttpServletRequest request,
                                                HttpServletResponse response
    ) throws ParseException, JOSEException {
        authService.refreshToken(request, response);
        return ResponseEntity.ok(ApiResponse.success("REFRESH TOKEN SUCCESS"));
    }

    @PostMapping("/introspect")
    ResponseEntity<ApiResponse<?>> introspect(HttpServletRequest request
    ) throws ParseException, JOSEException {

        var intro = authService.introspect(authService.getTokenFromCookie(request, "access_token"));
        return ResponseEntity.ok(new ApiResponse<>(200, "INTROSPECT", intro));
    }

    @PostMapping("/logout")
    ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request,
                                          HttpServletResponse response
    ) throws ParseException {
        authService.logout(request, response);
        return ResponseEntity.ok(ApiResponse.success("LOGOUT SUCCESS"));
    }

    @PatchMapping("/verify-account")
    ResponseEntity<ApiResponse<?>> verifyAccount(
            @RequestParam("token") String token
    ) {
        verificationTokenService.verifyAccount(token);
        return ResponseEntity.ok(new ApiResponse<>(200, "VERIFY TOKEN ACCOUNT", null));
    }
}
