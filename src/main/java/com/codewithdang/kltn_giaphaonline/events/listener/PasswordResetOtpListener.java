package com.codewithdang.kltn_giaphaonline.events.listener;

import com.codewithdang.kltn_giaphaonline.config.fe.FrontendProperties;
import com.codewithdang.kltn_giaphaonline.dto.request.email.ResetPasswordEmail;
import com.codewithdang.kltn_giaphaonline.events.producer.EmailProducer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordResetOtpListener {

    EmailProducer emailProducer;
    FrontendProperties frontendProperties;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ResetPasswordEmail event) {
        log.info("Reset password OTP received: {}", event.getOtp());
        emailProducer.sendEmail(event);
    }
}
