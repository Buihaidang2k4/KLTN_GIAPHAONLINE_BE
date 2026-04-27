package com.codewithdang.kltn_giaphaonline.service.forgot_password;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    
    @Override
    public void sendOTP(String email, String requestIp) {

    }

    @Override
    public void verifyOTP(String otpHash) {

    }
}
