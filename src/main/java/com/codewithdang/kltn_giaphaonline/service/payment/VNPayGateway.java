package com.codewithdang.kltn_giaphaonline.service.payment;

import com.codewithdang.kltn_giaphaonline.config.payment.VNPayConfig;
import com.codewithdang.kltn_giaphaonline.entity.Payment;
import com.codewithdang.kltn_giaphaonline.enums.PaymentProvider;
import com.codewithdang.kltn_giaphaonline.utils.VNPayUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class VNPayGateway implements PaymentGateway {
    VNPayConfig payConfig;

    @Override
    public PaymentProvider getProvider() {
        return PaymentProvider.VNPAY;
    }

    /***
     * generate payment url
     * @param payment
     * @param ipAddress
     * @param bankCode
     * @return
     */
    @Override
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

    @Override
    public boolean verifyCallback(Map<String, String> params) {
        return VNPayUtil.verifySignature(params, payConfig.getSecretKey());
    }

    @Override
    public String getMerchantTransactionId(Map<String, String> params) {
        return params.get("vnp_TxnRef");
    }

    @Override
    public String getResponseCode(Map<String, String> params) {
        return params.get("vnp_ResponseCode");
    }

    @Override
    public String getTransactionStatus(Map<String, String> params) {
        return params.get("vnp_TransactionStatus");
    }

    @Override
    public String getProviderTransactionId(Map<String, String> params) {
        return params.get("vnp_TransactionNo");
    }

    @Override
    public String getBankCode(Map<String, String> params) {
        return params.get("vnp_BankCode");
    }

    @Override
    public String getBankTransactionNo(Map<String, String> params) {
        return params.get("vnp_BankTranNo");
    }

    @Override
    public BigDecimal getPaidAmount(Map<String, String> params) {
        return new BigDecimal(params.get("vnp_Amount"))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }


    private String createDate() {
        SimpleDateFormat formatter =
                new SimpleDateFormat("yyyyMMddHHmmss");

        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        return formatter.format(new Date());
    }
}
