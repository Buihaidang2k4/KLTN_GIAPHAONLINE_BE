package com.codewithdang.kltn_giaphaonline.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // config main
    public static final String EMAIL_QUEUE = "email.queue";
    public static final String EMAIL_EXCHANGE = "email.exchange";
    public static final String EMAIL_ROUTING_KEY = "email.send";

    // config handle error
    public static final String EMAIL_DLX = "email.dlx";
    public static final String EMAIL_DLQ = "email.dead.letter.queue";
    public static final String EMAIL_DL_ROUTING_KEY = "email.dead.letter";


    @Bean
    Queue emailQueue() {
        // tạo một hàng đợi
        return QueueBuilder.durable(EMAIL_QUEUE)
                .withArgument("x-dead-letter-exchange", EMAIL_DLX)
                .withArgument("x-dead-letter-routing-key", EMAIL_DL_ROUTING_KEY)
                .withArgument("x-message-ttl", 600000)
                .build();
    }

    // điều hướng mail
    @Bean
    DirectExchange emailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE);
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_ROUTING_KEY);
    }

    // 4. Dead Letter Queue (Nơi lưu trữ các mail gửi lỗi)
    @Bean
    Queue deadLetterQueue() {
        return QueueBuilder.durable(EMAIL_DLQ).build();
    }

    // 5. Dead Letter Exchange
    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange(EMAIL_DLX);
    }

    // 6. Binding cho Dead Letter (Nối DLX với DLQ)
    @Bean
    Binding dlxBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(EMAIL_DL_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jacksonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jacksonConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonConverter());

        factory.setAdviceChain(RetryInterceptorBuilder.stateless()
                .maxAttempts(5)
                .backOffOptions(1000, 2.0, 10000)
                .build()
        );

        return factory;
    }


}
