package com.codewithdang.kltn_giaphaonline.event.consumer.handler;

import com.codewithdang.kltn_giaphaonline.dto.request.email.ResetPasswordEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Component
public class EmailForgotPasswordOTPHandler extends AbstractEmailHandler<ResetPasswordEmail> {

    public EmailForgotPasswordOTPHandler(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        super(mailSender, templateEngine);
    }

    @Override
    public void handle(ResetPasswordEmail email) throws Exception {
        Context context = new Context();
        context.setVariable("otp", email.getOtp());

        sendHtml(
                email.getToEmail(),
                email.getSubject(),
                "email/forgot-password",
                context
        );
    }
}
