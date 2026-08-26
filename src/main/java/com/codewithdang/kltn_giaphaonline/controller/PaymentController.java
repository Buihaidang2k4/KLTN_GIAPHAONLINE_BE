package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PaymentCreateRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PaymentRes;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
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
@RequestMapping(ApiPath.Payment.BASE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Payment Management")
public class PaymentController {

    PaymentService paymentService;

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PaymentRes>>> getAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Payment.GET_ALL_PAYMENTS_SUCCESS,
                paymentService.getAll(pageable)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Payment.BY_FAMILY)
    public ResponseEntity<ApiResponse<PageResponse<PaymentRes>>> getAllByFamilyId(
            @PathVariable Long familyId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Payment.GET_ALL_PAYMENTS_BY_FAMILY_ID_SUCCESS,
                paymentService.getAllByFamilyId(familyId, pageable)));
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.Payment.BY_ID)
    public ResponseEntity<ApiResponse<String>> deletePayment(@PathVariable Long paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Payment.DELETE_PAYMENT_SUCCESS, "OK"));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Payment.TRANSACTION_BY_ID)
    public ResponseEntity<ApiResponse<PaymentRes>> getByTransactionId(@PathVariable String transactionId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Payment.GET_PAYMENT_BY_TRANSACTION_ID_SUCCESS,
                paymentService.getByTransactionId(transactionId)));
    }

}
