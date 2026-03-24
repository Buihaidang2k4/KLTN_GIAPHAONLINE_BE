package com.codewithdang.kltn_giaphaonline.event.producer;

import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailBase;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractEmailHandler<T extends EmailBase>
        implements EmailHandler<T> {

    JavaMailSender mailSender;
    SpringTemplateEngine templateEngine;

    @Override
    public abstract Class<T> supportType();

    @Override
    public abstract void handle(T email) throws Exception;


    protected void sendHtml(
            String toEmail,
            String subject,
            String template,
            Context context
    ) throws MessagingException {
        // template child
        String childContent = templateEngine.process(template, context);

        // inject base
        Context base = new Context();
        base.setVariable("content", childContent);

        // base
        String baseContent = templateEngine.process("email/base", base);

        // 4. Gửi Mail
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(baseContent, true);

        // image logo
        helper.addInline("LogoGiaPha",
                new ClassPathResource("static/images/Logo_GiaPha.png"),
                "image/png");

        mailSender.send(mimeMessage);
    }

}
