package com.codewithdang.kltn_giaphaonline.events.consumer.handler;

import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailInvitationAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@Slf4j
public class EmailInvitationAccountHandler extends AbstractEmailHandler<EmailInvitationAccount> {

    public EmailInvitationAccountHandler(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        super(mailSender, templateEngine);
    }

    @Override
    public void handle(EmailInvitationAccount email) throws Exception {

        Context context = new Context();
        context.setVariable("fullName", email.getSenderFullName());
        context.setVariable("familyName", email.getFamilyName());
        context.setVariable("invitationLink", email.getInvitationToken());
        context.setVariable("expiryHours", email.getExpiryHours());
        context.setVariable("personalMessage", email.getPersonalMessage());

        sendHtml(
                email.getToEmail(),
                email.getSubject(),
                "email/invitation-account",
                context
        );
    }
}
