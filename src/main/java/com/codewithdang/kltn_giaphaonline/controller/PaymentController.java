package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PaymentCreateRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PaymentRes;
import com.codewithdang.kltn_giaphaonline.enums.PaymentProvider;
import com.codewithdang.kltn_giaphaonline.service.payment.PaymentApplicationService;
import com.codewithdang.kltn_giaphaonline.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Payment Management")
public class PaymentController {

    PaymentService paymentService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PaymentRes>>> getAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_ALL_PAYMENTS_SUCCESS",
                paymentService.getAll(pageable)));
    }

    @GetMapping("/family/{familyId}")
    public ResponseEntity<ApiResponse<PageResponse<PaymentRes>>> getAllByFamilyId(
            @PathVariable Long familyId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_ALL_PAYMENTS_BY_FAMILY_ID_SUCCESS",
                paymentService.getAllByFamilyId(familyId, pageable)));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<String>> deletePayment(@PathVariable Long paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success(200, "DELETE_PAYMENT_SUCCESS", "OK"));
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<ApiResponse<PaymentRes>> getByTransactionId(@PathVariable String transactionId) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_PAYMENT_BY_TRANSACTION_ID_SUCCESS",
                paymentService.getByTransactionId(transactionId)));
    }

}
