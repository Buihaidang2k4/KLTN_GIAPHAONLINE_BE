package com.codewithdang.kltn_giaphaonline.events.listener;

import com.codewithdang.kltn_giaphaonline.config.fe.FrontendProperties;
import com.codewithdang.kltn_giaphaonline.dto.event.UserRegisteredEvent;
import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailVerifyAccount;
import com.codewithdang.kltn_giaphaonline.events.producer.EmailProducer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRegisteredListener {

    EmailProducer emailProducer;

    // The event only begins after the transaction is complete.
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserRegisteredEvent event) {


        EmailVerifyAccount emailVerifyAccount =
                EmailVerifyAccount.builder()
                        .toEmail(event.email())
                        .fullName(event.fullName())
                        .subject("XÁC THỰC TÀI KHOẢN CỦA BẠN")
                        .verifyUrl(event.verificationToken())
                        .build();

        emailProducer.sendEmail(emailVerifyAccount);
    }
}
