package com.codewithdang.kltn_giaphaonline.event.consumer;

import com.codewithdang.kltn_giaphaonline.config.rabbitmq.RabbitMQConfig;
import com.codewithdang.kltn_giaphaonline.dto.request.email.*;
import com.codewithdang.kltn_giaphaonline.event.consumer.handler.EmailOTPHandler;
import com.codewithdang.kltn_giaphaonline.event.consumer.handler.EmailVerifyAccountHandler;
import com.codewithdang.kltn_giaphaonline.event.consumer.handler.EmailWelcomeHandler;
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
    public void handleInvitationMemberEmail(EmailInvitationAccount invitationAccount) {
        log.info("Received  an email inviting me to join the family. {} ", invitationAccount.getToEmail());

    }

    @RabbitHandler(isDefault = true)
    public void handleUnknown(Object payload) {
        throw new AmqpRejectAndDontRequeueException(
                "Unsupported payload type: " + payload.getClass().getName()
        );
    }

}
