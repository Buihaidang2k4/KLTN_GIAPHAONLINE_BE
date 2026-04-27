package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.LoginReq;
import com.codewithdang.kltn_giaphaonline.dto.request.RegisterByInvitationReq;
import com.codewithdang.kltn_giaphaonline.dto.request.RegisterReq;
import com.codewithdang.kltn_giaphaonline.dto.request.ResetPasswordReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.LoginRes;
import com.codewithdang.kltn_giaphaonline.dto.response.RegisterRes;
import com.codewithdang.kltn_giaphaonline.service.account.AccountService;
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
    ResponseEntity<ApiResponse<LoginRes>> login(@Valid @RequestBody LoginReq loginReq,
                                                HttpServletResponse response
    ) throws ParseException {
        return ResponseEntity.ok(
                ApiResponse.success(200, "LOGIN_SUCCESS", authService.authenticate(loginReq, response))
        );
    }

    @PostMapping("/register")
    ResponseEntity<ApiResponse<RegisterRes>> register(@Valid @RequestBody RegisterReq registerReq, HttpServletRequest request) {
        String requestIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        return ResponseEntity.ok(
                ApiResponse.success(200, "REGISTER_SUCCESS", authService.register(registerReq, requestIp, userAgent))
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

    @PostMapping("/verify-account/{token-verify}")
    ResponseEntity<ApiResponse<Void>> verifyAccount(@PathVariable("token-verify") String token) {
        verificationTokenService.verifyAccount(token);
        return ResponseEntity.ok(
                ApiResponse.success(200, "VERIFY_ACCOUNT_SUCCESS", null)
        );
    }

    @PostMapping("/re-send-token-verify/{email}")
    ResponseEntity<ApiResponse<Void>> reRendTokenVerifyAccount(@PathVariable("email") String email,
                                                               HttpServletRequest request
    ) {
        String requestIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        verificationTokenService.reSendVerificationToken(email, requestIp, userAgent);
        return ResponseEntity.ok(
                ApiResponse.success(200, "RESEND_TOKEN_VERIFY_ACCOUNT_SUCCESS", null)
        );
    }

    @PostMapping("/forgot-password-send-otp/{email}")
    ResponseEntity<ApiResponse<Void>> forgotPasswordSendOTP(@PathVariable("email") String email,
                                                            HttpServletRequest request
    ) {
        String requestIp = request.getRemoteAddr();
        authService.forgotPasswordSendOTP(email, requestIp);
        return ResponseEntity.ok(
                ApiResponse.success(200, "FORGOT_PASSWORD_SEND_OTP_SUCCESS", null)
        );
    }


    @PostMapping("/forgot-password-resend-otp/{email}")
    ResponseEntity<ApiResponse<Void>> resendOTPForgotPassword(@PathVariable("email") String email,
                                                              HttpServletRequest request) {
        String requestIp = request.getRemoteAddr();
        authService.resendOTPForgotPassword(email, requestIp);
        return ResponseEntity.ok(
                ApiResponse.success(200, "FORGOT_PASSWORD_RESEND_OTP_SUCCESS", null)
        );
    }

    @PostMapping("/verify-forgot-password-otp/{otp}")
    ResponseEntity<ApiResponse<Void>> verifyForgotPasswordOtp(@PathVariable("otp") String otp) {
        authService.verifyForgotPasswordOtpHash(otp);
        return ResponseEntity.ok(
                ApiResponse.success(200, "VERIFY_FORGOT_PASSWORD_OTP_HASH_SUCCESS", null)
        );
    }

    @PostMapping("/reset-password")
    ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody ResetPasswordReq req) {
        authService.resetPasswordWithOtp(req);
        return ResponseEntity.ok(
                ApiResponse.success(200, "RESET_PASSWORD_SUCCESS", null)
        );
    }


}