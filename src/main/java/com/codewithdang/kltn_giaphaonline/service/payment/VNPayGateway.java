package com.codewithdang.kltn_giaphaonline.service.payment;

import com.codewithdang.kltn_giaphaonline.config.payment.VNPayConfig;
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
import com.codewithdang.kltn_giaphaonline.utils.VNPayUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class VNPayServiceImpl {
    VNPayConfig payConfig;
    PaymentRepo paymentRepo;
    AccountRepo accountRepo;
    FamilyRepo familyRepo;
    SubscriptionPlanRepo planRepo;

    PaymentService paymentService;
    FamilySubscriptionService familySubscriptionService;
    

    @Transactional
    public PaymentCreateRes handlePayment(Long familyId, Long subscriptionPlanId, PaymentProvider provider, String bankCode, String ipAddress) {
        Account account = getCurrentAccount();
        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));
        SubscriptionPlan plan = planRepo.findById(subscriptionPlanId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

        if (!Boolean.TRUE.equals(plan.getIsActive())) {
            throw new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_ACTIVE);
        }

        // TODO: check admin family buy
//        if (!familyMemberRepo.existsByFamilyAndAccount(family, account)) {
//            throw new AppException(ErrorCode.FORBIDDEN);
//        }
        // checkFamilyAdmin(account,family);

        // create payment
        Payment payment = paymentService.createPayment(account, plan, family, provider);
        // create payment url
        String url = createPaymentUrl(payment, ipAddress, bankCode);

        return PaymentCreateRes.builder()
                .paymentId(payment.getPaymentId())
                .paymentUrl(url)
                .build();
    }

    /***
     * generate payment url
     * @param payment
     * @param ipAddress
     * @param bankCode
     * @return
     */
    public String createPaymentUrl(Payment payment, String ipAddress, String bankCode) {
        Map<String, String> params = new HashMap<>();

        params.put("vnp_Version", payConfig.getVnp_Version());
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", payConfig.getVnp_TmnCode());
        params.put("vnp_CurrCode", "VND");

        String amount = payment.getAmount()
                .multiply(BigDecimal.valueOf(100))
                .setScale(0).toPlainString();

        params.put("vnp_Amount", amount);
        params.put("vnp_TxnRef", payment.getMerchantTransactionId());
        params.put("vnp_OrderInfo", "Thanh toan goi subscription");
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", payConfig.getVnp_ReturnUrl());
        params.put("vnp_IpAddr", ipAddress);
        params.put("vnp_CreateDate", createDate());
        if (bankCode != null && !bankCode.isBlank()) {
            params.put("vnp_BankCode", bankCode);
        }

        String query = VNPayUtil.buildQuery(params);

        String hash = VNPayUtil.hmacSHA512(payConfig.getSecretKey(), query);
        return payConfig.getVnp_PayUrl() + "?" + query + "&vnp_SecureHash=" + hash;
    }

    /***
     * handle callback from VNPay
     * @param params
     * @return
     */
    @Transactional
    public VNPayCallbackRes handleCallback(Map<String, String> params) {
        boolean validSignature = VNPayUtil.verifySignature(params, payConfig.getSecretKey());

        if (!validSignature) {
            return VNPayCallbackRes.builder()
                    .success(false)
                    .message("Chữ ký VNPay không hợp lệ")
                    .responseCode(params.get("vnp_ResponseCode"))
                    .transactionId(params.get("vnp_TxnRef"))
                    .build();
        }

        String merchantTransactionId = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");
        String transactionStatus = params.get("vnp_TransactionStatus");
        String providerTransactionId = params.get("vnp_TransactionNo");
        String bankTranNo = params.get("vnp_BankTranNo");
        String bankCode = params.get("vnp_BankCode");

        Payment payment = paymentRepo.findByMerchantTransactionId(merchantTransactionId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return VNPayCallbackRes.builder()
                    .success(true)
                    .message("Payment đã được xử lý trước đó")
                    .transactionId(merchantTransactionId)
                    .responseCode(responseCode)
                    .build();
        }

        payment.setMerchantTransactionId(merchantTransactionId);
        payment.setProviderTransactionId(providerTransactionId);
        payment.setRawCallback(params.toString());
        payment.setBankCode(bankCode);
        payment.setBankTransactionNo(bankTranNo);

        BigDecimal callbackAmount = new BigDecimal(params.get("vnp_Amount"))
                .divide(BigDecimal.valueOf(100));

        if (payment.getAmount().compareTo(callbackAmount) != 0) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Số tiền không khớp");
            paymentRepo.save(payment);

            return VNPayCallbackRes.builder()
                    .success(false)
                    .message("Số tiền thanh toán không khớp")
                    .transactionId(merchantTransactionId)
                    .responseCode(responseCode)
                    .build();
        }

        if ("00".equals(responseCode) && "00".equals(transactionStatus)) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(Instant.now());

            //  activate và update FamilySubscription
            FamilySubscription subscription = familySubscriptionService.activateSubscription(payment);
            payment.setFamilySubscription(subscription);

            paymentRepo.save(payment);

            return VNPayCallbackRes.builder()
                    .success(true)
                    .message("Thanh toán thành công")
                    .transactionId(merchantTransactionId)
                    .responseCode(responseCode)
                    .build();
        }


        payment.setStatus(PaymentStatus.FAILED);
        payment.setFailureReason("VNPay responseCode=" + responseCode + ", transactionStatus=" + transactionStatus);
        paymentRepo.save(payment);

        return VNPayCallbackRes.builder()
                .success(false)
                .message("Thanh toán thất bại")
                .transactionId(merchantTransactionId)
                .responseCode(responseCode)
                .build();
    }

    private String createDate() {
        SimpleDateFormat formatter =
                new SimpleDateFormat("yyyyMMddHHmmss");

        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        return formatter.format(new Date());
    }

    private Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return accountRepo.findByEmail(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
    }
}
