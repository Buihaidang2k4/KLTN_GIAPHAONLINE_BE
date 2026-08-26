package com.codewithdang.kltn_giaphaonline.utils;

import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final AccountRepo accountRepo;

    public Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return accountRepo.findByEmail(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
    }

    public Optional<Account> getCurrentAccountOptional() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
                return Optional.empty();
            }
            return accountRepo.findByEmail(authentication.getName());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String getCurrentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }
}
