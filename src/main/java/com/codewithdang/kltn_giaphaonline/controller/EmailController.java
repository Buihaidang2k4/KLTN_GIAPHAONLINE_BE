package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailOTP;
import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailWelcome;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.event.producer.EmailProducer;
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
public class EmailController {

    EmailProducer producer;

    @PostMapping("/otp")
    public ResponseEntity<ApiResponse<String>> send(@RequestBody EmailOTP request) {
        producer.sendEmail(request);
        return ResponseEntity.ok(
                ApiResponse.success("email sent successfully")
        );
    }

    @PostMapping("/welcome")
    public ResponseEntity<ApiResponse<String>> send(@RequestBody EmailWelcome request) {
        producer.sendEmail(request);
        return ResponseEntity.ok(
                ApiResponse.success("email sent successfully")
        );
    }
}
