package com.codewithdang.kltn_giaphaonline.service.account_verification_token;

import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.AccountVerificationToken;

public interface AccountVerificationTokenService {
    AccountVerificationToken createVerificationToken(Account account, String requestedIp, String userAgent);

    void verifyAccount(String tokenVerify);
}
