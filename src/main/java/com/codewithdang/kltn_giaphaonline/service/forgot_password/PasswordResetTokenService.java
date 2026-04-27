package com.codewithdang.kltn_giaphaonline.service.forgot_password;

import com.codewithdang.kltn_giaphaonline.entity.Account;

public interface PasswordResetTokenService {
    void sendOTP(String email, String requestIp, Account account);

    void verifyOTP(String otpHash);

    boolean isOtpVerified(String otp);
}
