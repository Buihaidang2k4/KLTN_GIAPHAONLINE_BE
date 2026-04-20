package com.codewithdang.kltn_giaphaonline.event.consumer.handler;

import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailVerifyAccount;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
public class EmailVerifyAccountHandler extends AbstractEmailHandler<EmailVerifyAccount> {

    public EmailVerifyAccountHandler(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        super(mailSender, templateEngine);
    }

    @Override
    public void handle(EmailVerifyAccount email) throws Exception {
        Context context = new Context();
        context.setVariable("fullName", email.getFullName());
        context.setVariable("verifyToken", email.getVerifyUrl());

        sendHtml(
                email.getToEmail(),
                email.getSubject(),
                "email/verify-account",
                context
        );
    }
}
