package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.AuthReq;
import com.codewithdang.kltn_giaphaonline.dto.request.RegisterByInvitationReq;
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
        return ResponseEntity.ok(
                ApiResponse.success(200, "LOGIN_SUCCESS", authService.authenticate(authReq, response))
        );
    }

    @PostMapping("/register")
    ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterReq registerReq, HttpServletRequest request) {
        String requestIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        authService.register(registerReq, requestIp, userAgent);
        return ResponseEntity.ok(
                ApiResponse.success(200, "REGISTER_SUCCESS", null)
        );
    }

    @PostMapping("/register-by-invitation")
    public ResponseEntity<ApiResponse<Void>> registerByInvitation(
            @RequestBody @Valid RegisterByInvitationReq request,
            HttpServletRequest httpServletRequest
    ) {
        String remoteAddr = httpServletRequest.getRemoteAddr();
        String userAgent = httpServletRequest.getHeader("User-Agent");

        authService.registerByInvitation(request, remoteAddr, userAgent);

        return ResponseEntity.ok(
                ApiResponse.success(200,
                        "Đăng ký tài khoản thành công. Vui lòng kiểm tra email để xác thực tài khoản.",
                        null)
        );
    }


    @PostMapping("/refresh-token")
    ResponseEntity<ApiResponse<Void>> refreshToken(HttpServletRequest request,
                                                   HttpServletResponse response
    ) throws ParseException, JOSEException {
        authService.refreshToken(request, response);
        return ResponseEntity.ok(
                ApiResponse.success(200, "REFRESH_TOKEN_SUCCESS", null)
        );
    }

    @PostMapping("/introspect")
    ResponseEntity<ApiResponse<?>> introspect(HttpServletRequest request
    ) throws ParseException, JOSEException {
        var intro = authService.introspect(authService.getTokenFromCookie(request, "access_token"));
        return ResponseEntity.ok(
                ApiResponse.success(200, "INTROSPECT_SUCCESS", intro)
        );
    }

    @PostMapping("/logout")
    ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request,
                                             HttpServletResponse response
    ) throws ParseException {
        authService.logout(request, response);
        return ResponseEntity.ok(
                ApiResponse.success(200, "LOGOUT_SUCCESS", null)
        );
    }

    @PatchMapping("/verify-account/{token-verify}")
    ResponseEntity<ApiResponse<Void>> verifyAccount(@PathVariable("token-verify") String token) {
        verificationTokenService.verifyAccount(token);
        return ResponseEntity.ok(
                ApiResponse.success(200, "VERIFY_ACCOUNT_SUCCESS", null)
        );
    }

}