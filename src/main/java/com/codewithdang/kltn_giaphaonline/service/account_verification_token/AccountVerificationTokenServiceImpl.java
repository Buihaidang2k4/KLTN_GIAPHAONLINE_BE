package com.codewithdang.kltn_giaphaonline.service.account_verification_token;

import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.AccountVerificationToken;
import com.codewithdang.kltn_giaphaonline.enums.AccountStatus;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.AccountVerificationTokenRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountVerificationTokenServiceImpl implements AccountVerificationTokenService {
    AccountVerificationTokenRepo accountVerificationTokenRepo;
    AccountRepo accountRepo;

    @Override
    public AccountVerificationToken createVerificationToken(Account account, String requestedIp, String userAgent) {
        String token = UUID.randomUUID().toString();

        AccountVerificationToken verificationToken = AccountVerificationToken.builder()
                .account(account)
                .token(token)
                .otpCode(null)
                .expiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .verifiedAt(null)
                .isUsed(false)
                .requestedIp(requestedIp)
                .userAgent(userAgent)
                .build();

        return accountVerificationTokenRepo.save(verificationToken);
    }

    @Override
    public void verifyAccount(String tokenVerify) {
        AccountVerificationToken accountVerificationToken = accountVerificationTokenRepo.findByToken(tokenVerify)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_VERIFICATION_TOKEN_NOT_FOUND));

        if (Boolean.TRUE.equals(accountVerificationToken.getIsUsed()))
            throw new AppException(ErrorCode.ACCOUNT_VERIFICATION_TOKEN_ALREADY_USED);

        if (accountVerificationToken.getExpiresAt().isBefore(Instant.now()))
            throw new AppException(ErrorCode.ACCOUNT_VERIFICATION_TOKEN_EXPIRED);

        Account account = accountVerificationToken.getAccount();

        if (account.getAccountStatus() == AccountStatus.ACTIVE)
            throw new AppException(ErrorCode.ACCOUNT_ALREADY_VERIFIED);

        account.setAccountStatus(AccountStatus.ACTIVE);
        accountVerificationToken.setIsUsed(true);
        accountVerificationToken.setVerifiedAt(Instant.now());

        accountRepo.save(account);
        accountVerificationTokenRepo.save(accountVerificationToken);
    }
}
