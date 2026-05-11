package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.fe.FrontendProperties;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PaymentCreateRes;
import com.codewithdang.kltn_giaphaonline.dto.response.VNPayCallbackRes;
import com.codewithdang.kltn_giaphaonline.enums.PaymentProvider;
import com.codewithdang.kltn_giaphaonline.service.payment.PaymentApplicationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/payments/vnpay")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "VNPay Payment")
public class VNPayController {

    PaymentApplicationService paymentApplicationService;
    FrontendProperties frontendProperties;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentCreateRes>> createPayment(
            @RequestParam Long familyId,
            @RequestParam Long subscriptionPlanId,
            @RequestParam(required = false, defaultValue = "NCB") String bankCode,
            HttpServletRequest request) {
        String ipAddress = getIpAddress(request);
        return ResponseEntity.ok(ApiResponse.success(201, "CREATE_PAYMENT_SUCCESS",
                paymentApplicationService.handlePayment(
                        familyId, subscriptionPlanId, PaymentProvider.VNPAY, bankCode, ipAddress)));
    }

    @GetMapping("/callback")
    public ResponseEntity<Void> callback(
            @RequestParam Map<String, String> params) throws JsonProcessingException {
        VNPayCallbackRes result = paymentApplicationService.handleCallback(PaymentProvider.VNPAY, params);

        // redirect về frontend kèm kết quả
        String redirectUrl = UriComponentsBuilder
                .fromHttpUrl(frontendProperties.getBaseUrl() + "/family/chi-tiet-thanh-toan")
                .queryParam("success", result.isSuccess())
                .queryParam("transactionId", result.getTransactionId())
                .queryParam("responseCode", result.getResponseCode())
                .build().toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, redirectUrl);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        // VNPay không chấp nhận IPv6, đổi ::1 về 127.0.0.1
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

}