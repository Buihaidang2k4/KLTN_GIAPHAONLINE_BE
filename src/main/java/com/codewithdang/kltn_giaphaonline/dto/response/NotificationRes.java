package com.codewithdang.kltn_giaphaonline.dto.response;

import com.codewithdang.kltn_giaphaonline.enums.NotificationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationRes {
    Long notificationId;
    Long recipientAccountId;
    String recipientName;
    Long senderAccountId;
    String senderName;
    NotificationType type;
    String title;
    String content;
    Boolean isRead;
    Long referenceId;
    String referenceType;
    String actionUrl;
    Instant createdAt;
    Instant readAt;
}
