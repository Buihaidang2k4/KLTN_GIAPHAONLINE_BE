package com.codewithdang.kltn_giaphaonline.event.producer;

import com.codewithdang.kltn_giaphaonline.config.fe.FrontendProperties;
import com.codewithdang.kltn_giaphaonline.dto.event.UserRegisteredEvent;
import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailVerifyAccount;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRegisteredListener {

    EmailProducer emailProducer;
    FrontendProperties frontendProperties;

    // The event only begins after the transaction is complete.
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserRegisteredEvent event) {

        String verifyUrl = UriComponentsBuilder
                .fromHttpUrl(frontendProperties.getBaseUrl()) // url
                .path(frontendProperties.getVerifyEndpoint())
                .queryParam("token", event.verificationToken())
                .toUriString();

        EmailVerifyAccount emailVerifyAccount =
                EmailVerifyAccount.builder()
                        .toEmail(event.email())
                        .fullName(event.fullName())
                        .subject("XÁC THỰC TÀI KHOẢN CỦA BẠN")
                        .verifyUrl(verifyUrl)
                        .build();

        emailProducer.sendEmail(emailVerifyAccount);
    }
}
