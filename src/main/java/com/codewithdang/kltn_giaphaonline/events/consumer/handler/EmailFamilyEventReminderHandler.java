package com.codewithdang.kltn_giaphaonline.events.consumer.handler;

import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailFamilyEventReminder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
public class EmailFamilyEventReminderHandler extends AbstractEmailHandler<EmailFamilyEventReminder> {

    public EmailFamilyEventReminderHandler(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        super(mailSender, templateEngine);
    }

    @Override
    public void handle(EmailFamilyEventReminder email) throws Exception {
        Context context = new Context();
        context.setVariable("recipientName", email.getRecipientName());
        context.setVariable("eventName", email.getEventName());
        context.setVariable("eventDate", email.getEventDate());
        context.setVariable("familyName", email.getFamilyName());
        context.setVariable("eventUrl", email.getEventUrl());
        context.setVariable("isToday", email.isToday());

        sendHtml(email.getToEmail(), email.getSubject(), "email/family-event-reminder", context);
    }
}
