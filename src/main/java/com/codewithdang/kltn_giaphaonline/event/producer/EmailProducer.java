package com.codewithdang.kltn_giaphaonline.event.producer;

import com.codewithdang.kltn_giaphaonline.config.rabbitmq.RabbitMQConfig;
import com.codewithdang.kltn_giaphaonline.dto.request.EmailTestReq;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class EmailProducer {
    RabbitTemplate rabbitTemplate;

    public void sendEmail(EmailTestReq req){
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EMAIL_EXCHANGE,
                RabbitMQConfig.EMAIL_ROUTING_KEY,
                req
        );
    }


}
