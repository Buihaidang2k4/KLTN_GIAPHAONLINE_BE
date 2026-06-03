package com.codewithdang.kltn_giaphaonline.events.consumer.handler;

import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailWelcome;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
public class EmailWelcomeHandler extends AbstractEmailHandler<EmailWelcome> {

    public EmailWelcomeHandler(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        super(mailSender, templateEngine);
    }

    @Override
    public void handle(EmailWelcome email) throws Exception {
        Context context = new Context();
        context.setVariable("fullName", email.getFullName());
        context.setVariable("loginUrl", email.getLoginUrl());

        sendHtml(
                email.getToEmail(),
                email.getSubject(),
                "email/welcome",
                context
        );
    }
}
