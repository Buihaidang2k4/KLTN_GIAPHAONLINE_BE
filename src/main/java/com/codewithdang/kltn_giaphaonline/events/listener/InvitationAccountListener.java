package com.codewithdang.kltn_giaphaonline.events.listener;

import com.codewithdang.kltn_giaphaonline.config.fe.FrontendProperties;
import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailInvitationAccount;
import com.codewithdang.kltn_giaphaonline.events.producer.EmailProducer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvitationAccountListener {

    EmailProducer emailProducer;
    FrontendProperties frontendProperties;

    // The event only begins after the transaction is complete.
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(EmailInvitationAccount event) {
        String invitationLink = UriComponentsBuilder
                .fromHttpUrl(frontendProperties.getBaseUrl()) // url
                .path(frontendProperties.getVerifyEndpoint())
                .path("/")
                .path(event.getInvitationToken())
                .toUriString();

        EmailInvitationAccount invitationAccount = EmailInvitationAccount.builder()
                .toEmail(event.getToEmail())
                .subject(event.getSubject())
                .invitationToken(invitationLink)
                .senderFullName(event.getSenderFullName())
                .familyName(event.getFamilyName())
                .personalMessage(event.getPersonalMessage())
                .expiryHours(event.getExpiryHours())
                .build();

        log.info("================ Invitation account received: {}", invitationAccount.getFamilyName());
        emailProducer.sendEmail(invitationAccount);
    }
}
