package com.codewithdang.kltn_giaphaonline.service.forgot_password;

public interface ForgotPasswordService {
    void sendOTP(String email, String requestIp);

    void verifyOTP(String otpHash);
}
