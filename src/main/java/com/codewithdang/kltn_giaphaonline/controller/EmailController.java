package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailOTP;
import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailWelcome;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.event.producer.EmailProducer;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Email Management")
public class EmailController {

    EmailProducer producer;

    @PostMapping("/otp")
    public ResponseEntity<ApiResponse<Void>> sendOtp(@RequestBody EmailOTP request) {
        producer.sendEmail(request);
        return ResponseEntity.ok(
                ApiResponse.success(200, "SEND_EMAIL_OTP_SUCCESS", null)
        );
    }

    @PostMapping("/welcome")
    public ResponseEntity<ApiResponse<Void>> sendWelcome(@RequestBody EmailWelcome request) {
        producer.sendEmail(request);
        return ResponseEntity.ok(
                ApiResponse.success(200, "SEND_EMAIL_WELCOME_SUCCESS", null)
        );
    }
}