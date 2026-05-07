package com.codewithdang.kltn_giaphaonline.service.payment;

import com.codewithdang.kltn_giaphaonline.entity.Payment;
import com.codewithdang.kltn_giaphaonline.enums.PaymentProvider;

import java.math.BigDecimal;
import java.util.Map;

public interface PaymentGateway {
    PaymentProvider getProvider();

    String createPaymentUrl(Payment payment, String ipAddress, String bankCode);

    boolean verifyCallback(Map<String, String> params);

    String getMerchantTransactionId(Map<String, String> params);

    String getResponseCode(Map<String, String> params);

    String getTransactionStatus(Map<String, String> params);

    String getProviderTransactionId(Map<String, String> params);

    String getBankCode(Map<String, String> params);

    String getBankTransactionNo(Map<String, String> params);

    BigDecimal getPaidAmount(Map<String, String> params);
}
