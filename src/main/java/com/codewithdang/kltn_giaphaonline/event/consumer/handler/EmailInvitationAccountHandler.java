package com.codewithdang.kltn_giaphaonline.event.consumer.handler;

import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailInvitationAccount;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
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
