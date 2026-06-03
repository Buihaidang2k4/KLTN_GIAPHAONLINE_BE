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
import org.springframework.mail.MailSendException;
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
    public void handleEmail(EmailOTP email) {
        log.info("Received OTP email for {}", email.getToEmail());
        safeHandle(() -> emailOTPHandler.handle(email), "OTP");
    }

    @RabbitHandler
    public void handleWelcome(EmailWelcome email) {
        log.info("Received welcome email for {}", email.getToEmail());
        safeHandle(() -> emailWelcomeHandler.handle(email), "Welcome");
    }

    @RabbitHandler
    public void handleEmailVerifyAccount(EmailVerifyAccount verifyAccount) {
        log.info("Received verify account email for {}", verifyAccount.getToEmail());
        safeHandle(() -> emailVerifyAccountHandler.handle(verifyAccount), "Verify Account");
    }

    @RabbitHandler
    public void handleInvitationMemberEmail(EmailInvitationAccount invitationAccount) {
        log.info("Received invitation email for {}", invitationAccount.getToEmail());
        safeHandle(() -> emailInvitationAccountHandler.handle(invitationAccount), "Invitation Account");
    }

    @RabbitHandler
    public void handleForgotPasswordEmail(ResetPasswordEmail resetPasswordEmail) {
        log.info("Received forgot password email for {}", resetPasswordEmail.getToEmail());
        safeHandle(() -> passwordOTPHandler.handle(resetPasswordEmail), "Forgot Password OTP");
    }

    @RabbitHandler(isDefault = true)
    public void handleUnknown(Object payload) {
        throw new AmqpRejectAndDontRequeueException(
                "Unsupported payload type: " + payload.getClass().getName()
        );
    }

    @FunctionalInterface
    private interface EmailHandler {
        void handle() throws Exception;
    }

    private void safeHandle(EmailHandler handler, String type) {
        try {
            handler.handle();
            log.info("{} email handled successfully", type);
        } catch (MailSendException e) {
            log.error("{} email failed: {}", type, e.getMessage(), e);
        } catch (Exception e) {
            log.error("{} email consumer failed", type, e);
        }
    }
}
