package com.codewithdang.kltn_giaphaonline.service.forgot_password;

import com.codewithdang.kltn_giaphaonline.dto.request.email.ResetPasswordEmail;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.PasswordResetToken;
import com.codewithdang.kltn_giaphaonline.enums.AccountStatus;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.repo.PasswordResetTokenRepo;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    PasswordResetTokenRepo passwordResetTokenRepo;
    ApplicationEventPublisher eventPublisher;


    @Override
    @Transactional
    public void sendOTP(String email, String requestIp, Account account) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        String otp = random.ints(7, 0, chars.length())
                .mapToObj(i -> String.valueOf(chars.charAt(i)))
                .collect(Collectors.joining());

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .otp(otp)
                .account(account)
                .expiresAt(Instant.now().plus(5, ChronoUnit.MINUTES))
                .requestedIp(requestIp)
                .requestedAt(Instant.now())
                .build();

        passwordResetTokenRepo.save(passwordResetToken);

        eventPublisher.publishEvent(
                ResetPasswordEmail.builder()
                        .toEmail(account.getEmail())
                        .otp(otp)
                        .subject("Reset Password OTP")
                        .build()
        );
    }

    @Override
    @Transactional
    public void verifyOTP(String otp) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepo.findByOtp(otp)
                .orElseThrow(() -> new AppException(ErrorCode.OTP_FORGOT_PASSWORD_NOT_EXISTED));

        if (Boolean.TRUE.equals(passwordResetToken.getIsSuccess()))
            throw new AppException(ErrorCode.OTP_FORGOT_PASSWORD_ALREADY_USED);

        if (passwordResetToken.getExpiresAt().isBefore(Instant.now()))
            throw new AppException(ErrorCode.OTP_FORGOT_PASSWORD_EXPIRED);

        if (passwordResetToken.getAccount().getAccountStatus() != AccountStatus.ACTIVE)
            throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);

        passwordResetToken.setIsSuccess(true);
        passwordResetToken.setUsedAt(Instant.now());
        passwordResetTokenRepo.save(passwordResetToken);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOtpVerified(String otp) {
        return passwordResetTokenRepo.isOtpVerified(otp);
    }

}
