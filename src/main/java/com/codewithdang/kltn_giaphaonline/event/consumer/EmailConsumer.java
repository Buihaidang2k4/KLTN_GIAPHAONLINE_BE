package com.codewithdang.kltn_giaphaonline.event.consumer;

import com.codewithdang.kltn_giaphaonline.config.rabbitmq.RabbitMQConfig;
import com.codewithdang.kltn_giaphaonline.dto.request.EmailTestReq;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailConsumer {
    JavaMailSender mailSender;
    SpringTemplateEngine templateEngine;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void consume(EmailTestReq req) throws MessagingException {
        try {
            // 1. Setup Data cho nội dung cụ thể (ví dụ: OTP hoặc Welcome)
            Context context = new Context();
            context.setVariable("name", req.name());
            context.setVariable("content", req.content());
            context.setVariable("toEmail", req.toEmail());
            context.setVariable("subject", req.subject());

            String childContent = templateEngine.process("email/welcome", context);

            // 3. Render file base.html và nhúng childContent vào
            Context baseContext = new Context();
            baseContext.setVariable("content", childContent);
            String finalHtml = templateEngine.process("email/base", baseContext);

            // 4. Gửi Mail
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(req.toEmail());
            helper.setSubject(req.subject());
            helper.setText(finalHtml, true); // true để hiểu đây là HTML

            mailSender.send(mimeMessage);
            log.info("Email đã gửi thành công đến: {}", req.toEmail());

        } catch (Exception e) {
            log.error("Lỗi khi xử lý gửi email: ", e);
            throw e; // Để RabbitMQ thực hiện cơ chế Retry nếu cần
        }
    }
}
