package com.codewithdang.kltn_giaphaonline.event.producer;

import com.codewithdang.kltn_giaphaonline.dto.event.UserRegisteredEvent;
import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailOTP;
import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailWelcome;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpMailAfterCommitListener {

    EmailProducer emailProducer;

    // The event only begins after the transaction is complete.
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserRegisteredEvent event){
        EmailWelcome email = new EmailWelcome();
        email.setToEmail(event.email());
        email.setFullName(event.fullName());
        email.setSubject("Chào mừng bạn đến với hệ thống");
        email.setLoginUrl("https://your-domain.com/login");

        emailProducer.sendEmail(email);
    }

}
