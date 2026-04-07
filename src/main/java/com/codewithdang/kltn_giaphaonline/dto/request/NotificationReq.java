package com.codewithdang.kltn_giaphaonline.dto.request;

import com.codewithdang.kltn_giaphaonline.enums.NotificationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationReq {
    Long recipientAccountId;
    Long senderAccountId;
    NotificationType type;
    String title;
    String content;
    Long referenceId;
    String referenceType;
    String actionUrl;
}
