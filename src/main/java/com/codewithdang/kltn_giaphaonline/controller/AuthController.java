package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.LoginReq;
import com.codewithdang.kltn_giaphaonline.dto.request.RegisterByInvitationReq;
import com.codewithdang.kltn_giaphaonline.dto.request.RegisterReq;
import com.codewithdang.kltn_giaphaonline.dto.request.ResetPasswordReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.LoginRes;
import com.codewithdang.kltn_giaphaonline.dto.response.RegisterRes;
import com.codewithdang.kltn_giaphaonline.dto.response.UserSessionRes;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
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
import java.util.List;

@RestController
@RequestMapping(ApiPath.Auth.BASE)
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Auth Management")
public class AuthController {
    AuthService authService;
    AccountVerificationTokenService verificationTokenService;

    @OperatorAction(CommonEnums.Operator.READ)
    @PostMapping(ApiPath.Auth.LOGIN)
    ResponseEntity<ApiResponse<LoginRes>> login(@Valid @RequestBody LoginReq loginReq,
                                                HttpServletResponse response
    ) throws ParseException {
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Auth.LOGIN_SUCCESS, authService.authenticate(loginReq, response))
        );
    }

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping(ApiPath.Auth.REGISTER)
    ResponseEntity<ApiResponse<RegisterRes>> register(@Valid @RequestBody RegisterReq registerReq, HttpServletRequest request) {
        String requestIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Auth.REGISTER_SUCCESS, authService.register(registerReq, requestIp, userAgent))
        );
    }

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping(ApiPath.Auth.REGISTER_BY_INVITATION)
    public ResponseEntity<ApiResponse<Void>> registerByInvitation(
            @PathVariable("token") String token,
            @RequestBody @Valid RegisterByInvitationReq request,
            HttpServletRequest httpServletRequest
    ) {
        String remoteAddr = httpServletRequest.getRemoteAddr();
        String userAgent = httpServletRequest.getHeader("User-Agent");

        authService.registerByInvitation(token, request, remoteAddr, userAgent);

        return ResponseEntity.ok(
                ApiResponse.success(200,
                        MessageConstantsVi.Auth.REGISTER_BY_INVITATION_SUCCESS,
                        null)
        );
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PostMapping(ApiPath.Auth.REFRESH_TOKEN)
    ResponseEntity<ApiResponse<Void>> refreshToken(HttpServletRequest request,
                                                   HttpServletResponse response
    ) throws ParseException, JOSEException {
        authService.refreshToken(request, response);
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Auth.REFRESH_TOKEN_SUCCESS, null)
        );
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @PostMapping(ApiPath.Auth.INTROSPECT)
    ResponseEntity<ApiResponse<?>> introspect(HttpServletRequest request
    ) throws ParseException, JOSEException {
        var intro = authService.introspect(authService.getTokenFromCookie(request, "access_token"));
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Auth.INTROSPECT_SUCCESS, intro)
        );
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @PostMapping(ApiPath.Auth.LOGOUT)
    ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request,
                                             HttpServletResponse response
    ) throws ParseException {
        authService.logout(request, response);
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Auth.LOGOUT_SUCCESS, null)
        );
    }

    @OperatorAction(CommonEnums.Operator.CANCEL)
    @PostMapping(ApiPath.Auth.LOGOUT_ALL)
    ResponseEntity<ApiResponse<Void>> logoutAll(HttpServletRequest request,
                                                HttpServletResponse response
    ) throws ParseException {
        authService.logoutAll(request, response);
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Auth.LOGOUT_ALL_SUCCESS, null)
        );
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Auth.SESSIONS)
    ResponseEntity<ApiResponse<List<UserSessionRes>>> getActiveSessions(
            HttpServletRequest request
    ) throws ParseException {
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Auth.GET_ACTIVE_SESSIONS_SUCCESS, authService.getActiveSessions(request))
        );
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PostMapping(ApiPath.Auth.VERIFY_ACCOUNT)
    ResponseEntity<ApiResponse<Void>> verifyAccount(@PathVariable("token-verify") String token) {
        verificationTokenService.verifyAccount(token);
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Auth.VERIFY_ACCOUNT_SUCCESS, null)
        );
    }

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping(ApiPath.Auth.RESEND_TOKEN_VERIFY)
    ResponseEntity<ApiResponse<Void>> reRendTokenVerifyAccount(@PathVariable("email") String email,
                                                               HttpServletRequest request
    ) {
        String requestIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        verificationTokenService.reSendVerificationToken(email, requestIp, userAgent);
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Auth.RESEND_TOKEN_VERIFY_ACCOUNT_SUCCESS, null)
        );
    }

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping(ApiPath.Auth.FORGOT_PASSWORD_SEND_OTP)
    ResponseEntity<ApiResponse<Void>> forgotPasswordSendOTP(@PathVariable("email") String email,
                                                            HttpServletRequest request
    ) {
        String requestIp = request.getRemoteAddr();
        authService.forgotPasswordSendOTP(email, requestIp);
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Auth.FORGOT_PASSWORD_SEND_OTP_SUCCESS, null)
        );
    }

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping(ApiPath.Auth.FORGOT_PASSWORD_RESEND_OTP)
    ResponseEntity<ApiResponse<Void>> resendOTPForgotPassword(@PathVariable("email") String email,
                                                              HttpServletRequest request) {
        String requestIp = request.getRemoteAddr();
        authService.resendOTPForgotPassword(email, requestIp);
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Auth.FORGOT_PASSWORD_RESEND_OTP_SUCCESS, null)
        );
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @PostMapping(ApiPath.Auth.VERIFY_FORGOT_PASSWORD_OTP)
    ResponseEntity<ApiResponse<Void>> verifyForgotPasswordOtp(@PathVariable("otp") String otp) {
        authService.verifyForgotPasswordOtpHash(otp);
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Auth.VERIFY_FORGOT_PASSWORD_OTP_HASH_SUCCESS, null)
        );
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PostMapping(ApiPath.Auth.RESET_PASSWORD)
    ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody ResetPasswordReq req) {
        authService.resetPasswordWithOtp(req);
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Auth.RESET_PASSWORD_SUCCESS, null)
        );
    }
}