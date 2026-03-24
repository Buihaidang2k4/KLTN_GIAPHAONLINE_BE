package com.codewithdang.kltn_giaphaonline.event.consumer.handler;

import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailBase;
import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailOTP;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
public class EmailOTPHandler extends AbstractEmailHandler<EmailOTP> {

    public EmailOTPHandler(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        super(mailSender, templateEngine);
    }

    @Override
    public void handle(EmailOTP email) throws Exception {
        Context context = new Context();
        context.setVariable("fullName", email.getFullName());
        context.setVariable("otp", email.getOtp());

        sendHtml(email.getToEmail(),
                email.getSubject(),
                "email/otp",
                context
        );
    }
}
