package com.codewithdang.kltn_giaphaonline.service.payment;

import com.codewithdang.kltn_giaphaonline.dto.response.PaymentCreateRes;
import com.codewithdang.kltn_giaphaonline.dto.response.VNPayCallbackRes;
import com.codewithdang.kltn_giaphaonline.entity.*;
import com.codewithdang.kltn_giaphaonline.enums.PaymentProvider;
import com.codewithdang.kltn_giaphaonline.enums.PaymentStatus;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyRepo;
import com.codewithdang.kltn_giaphaonline.repo.PaymentRepo;
import com.codewithdang.kltn_giaphaonline.repo.SubscriptionPlanRepo;
import com.codewithdang.kltn_giaphaonline.service.family_subscription.FamilySubscriptionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentApplicationService {

    AccountRepo accountRepo;
    FamilyRepo familyRepo;
    SubscriptionPlanRepo planRepo;
    PaymentRepo paymentRepo;

    PaymentService paymentService;
    FamilySubscriptionService familySubscriptionService;

    List<PaymentGateway> gateways;

    @Transactional
    public PaymentCreateRes handlePayment(
            Long familyId,
            Long subscriptionPlanId,
            PaymentProvider provider,
            String bankCode,
            String ipAddress
    ) {
        Account account = getCurrentAccount();

        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));

        SubscriptionPlan plan = planRepo.findById(subscriptionPlanId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

        if (!Boolean.TRUE.equals(plan.getIsActive())) {
            throw new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_ACTIVE);
        }

        Payment payment = paymentService.createPayment(account, plan, family, provider);

        PaymentGateway gateway = getGateway(provider);

        String paymentUrl = gateway.createPaymentUrl(payment, ipAddress, bankCode);

        return PaymentCreateRes.builder()
                .paymentId(payment.getPaymentId())
                .paymentUrl(paymentUrl)
                .build();
    }

    @Transactional
    public VNPayCallbackRes handleCallback(
            PaymentProvider provider,
            Map<String, String> params
    ) {
        PaymentGateway gateway = getGateway(provider);

        if (!gateway.verifyCallback(params)) {
            return VNPayCallbackRes.builder()
                    .success(false)
                    .message("Chữ ký thanh toán không hợp lệ")
                    .responseCode(gateway.getResponseCode(params))
                    .transactionId(gateway.getMerchantTransactionId(params))
                    .build();
        }

        String merchantTransactionId = gateway.getMerchantTransactionId(params);

        Payment payment = paymentRepo.findByMerchantTransactionId(merchantTransactionId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return VNPayCallbackRes.builder()
                    .success(true)
                    .message("Payment đã được xử lý trước đó")
                    .transactionId(merchantTransactionId)
                    .responseCode(gateway.getResponseCode(params))
                    .build();
        }

        BigDecimal callbackAmount = gateway.getPaidAmount(params);

        if (payment.getAmount().compareTo(callbackAmount) != 0) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Số tiền không khớp");
            paymentRepo.save(payment);

            return VNPayCallbackRes.builder()
                    .success(false)
                    .message("Số tiền thanh toán không khớp")
                    .transactionId(merchantTransactionId)
                    .responseCode(gateway.getResponseCode(params))
                    .build();
        }

        payment.setProviderTransactionId(gateway.getProviderTransactionId(params));
        payment.setBankCode(gateway.getBankCode(params));
        payment.setBankTransactionNo(gateway.getBankTransactionNo(params));
        payment.setRawCallback(params.toString());

        boolean success = "00".equals(gateway.getResponseCode(params))
                && "00".equals(gateway.getTransactionStatus(params));

        if (success) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(Instant.now());

            FamilySubscription subscription =
                    familySubscriptionService.activateSubscription(payment);

            payment.setFamilySubscription(subscription);
            paymentRepo.save(payment);

            return VNPayCallbackRes.builder()
                    .success(true)
                    .message("Thanh toán thành công")
                    .transactionId(merchantTransactionId)
                    .responseCode(gateway.getResponseCode(params))
                    .build();
        }

        payment.setStatus(PaymentStatus.FAILED);
        payment.setFailureReason(
                "Payment responseCode=" + gateway.getResponseCode(params)
                        + ", transactionStatus=" + gateway.getTransactionStatus(params)
        );
        paymentRepo.save(payment);

        return VNPayCallbackRes.builder()
                .success(false)
                .message("Thanh toán thất bại")
                .transactionId(merchantTransactionId)
                .responseCode(gateway.getResponseCode(params))
                .build();
    }

    private PaymentGateway getGateway(PaymentProvider provider) {
        return gateways.stream()
                .filter(gateway -> gateway.getProvider() == provider)
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_PROVIDER_NOT_SUPPORTED));
    }

    private Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        return accountRepo.findByEmail(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
    }
}