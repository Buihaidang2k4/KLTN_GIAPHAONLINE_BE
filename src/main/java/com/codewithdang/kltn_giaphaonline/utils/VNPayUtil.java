package com.codewithdang.kltn_giaphaonline.utils;

import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class VNPayUtil {
    private static final String HMAC_SHA512 = "HmacSHA512";

    public static boolean verifySignature(
            Map<String, String> params,
            String secretKey
    ) {
        String secureHash = params.get("vnp_SecureHash");

        if (secureHash == null || secureHash.isBlank()) {
            return false;
        }

        Map<String, String> verifyParams = new HashMap<>(params);

        verifyParams.remove("vnp_SecureHash");
        verifyParams.remove("vnp_SecureHashType");

        String query = buildQuery(verifyParams);
        String calculatedHash = hmacSHA512(secretKey, query);

        return secureHash.equalsIgnoreCase(calculatedHash);
    }

    public static String hmacSHA512(String secretKey, String data) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalArgumentException("Secret key không được để trống");
        }

        if (data == null) {
            throw new IllegalArgumentException("Data không được null");
        }

        try {
            Mac mac = Mac.getInstance(HMAC_SHA512);

            SecretKeySpec keySpec = new SecretKeySpec(
                    secretKey.getBytes(StandardCharsets.UTF_8),
                    HMAC_SHA512
            );

            mac.init(keySpec);

            byte[] hashBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hashBytes);

        } catch (Exception e) {
            throw new IllegalStateException("Không thể tạo HMAC SHA512", e);
        }
    }

    public static String buildQuery(Map<String, String> params) {
        return params.entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getValue().isBlank())
                .sorted(Map.Entry.comparingByKey())
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8)
                        + "=" +
                        URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }

    public static String getRandomNumber(int length) {
        Random random = new Random();
        String chars = "0123456789";
        StringBuffer sb = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }

}
