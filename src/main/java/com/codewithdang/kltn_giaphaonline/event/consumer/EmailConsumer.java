package com.codewithdang.kltn_giaphaonline.event.consumer;

import com.codewithdang.kltn_giaphaonline.config.rabbitmq.RabbitMQConfig;
import com.codewithdang.kltn_giaphaonline.dto.request.email.*;
import com.codewithdang.kltn_giaphaonline.event.consumer.handler.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailConsumer {

    EmailWelcomeHandler emailWelcomeHandler;
    EmailOTPHandler emailOTPHandler;
    EmailVerifyAccountHandler emailVerifyAccountHandler;
    EmailInvitationAccountHandler emailInvitationAccountHandler;
    EmailForgotPasswordOTPHandler passwordOTPHandler;

    @RabbitHandler
    public void handleEmail(EmailOTP email) throws Exception {
        log.info("Received OTP email for {}", email.getToEmail());
        emailOTPHandler.handle(email);
    }

    @RabbitHandler
    public void handleWelcome(EmailWelcome email) throws Exception {
        log.info("Received welcome email for {}", email.getToEmail());
        emailWelcomeHandler.handle(email);
    }

    @RabbitHandler
    public void handleEmailVerifyAccount(EmailVerifyAccount verifyAccount) throws Exception {
        log.info("Received verify account email for {}", verifyAccount.getToEmail());
        emailVerifyAccountHandler.handle(verifyAccount);
    }

    @RabbitHandler
    public void handleInvitationMemberEmail(EmailInvitationAccount invitationAccount) throws Exception {
        log.info("Received invitation email for {}", invitationAccount.getToEmail());
        emailInvitationAccountHandler.handle(invitationAccount);
    }

    @RabbitHandler
    public void handleForgotPasswordEmail(ResetPasswordEmail resetPasswordEmail) throws Exception {
        log.info("Received forgot password email for {}", resetPasswordEmail.getToEmail());
        passwordOTPHandler.handle(resetPasswordEmail);
    }

    @RabbitHandler(isDefault = true)
    public void handleUnknown(Object payload) {
        throw new AmqpRejectAndDontRequeueException(
                "Unsupported payload type: " + payload.getClass().getName()
        );
    }

}
