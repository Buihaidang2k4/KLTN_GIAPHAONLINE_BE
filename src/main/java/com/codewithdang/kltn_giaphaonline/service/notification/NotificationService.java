package com.codewithdang.kltn_giaphaonline.service.notification;

import com.codewithdang.kltn_giaphaonline.dto.request.NotificationReq;
import com.codewithdang.kltn_giaphaonline.dto.response.NotificationRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    NotificationRes createNotification(NotificationReq request);

    void createFamilyNotification(Long familyId, Long senderAccountId,
                                  NotificationType type, String title, String content,
                                  Long referenceId, String referenceType, String actionUrl);


    NotificationRes createNotification(
            Long recipientAccountId,
            Long senderAccountId,
            NotificationType type,
            String title,
            String content,
            Long referenceId,
            String referenceType,
            String actionUrl
    );

    PageResponse<NotificationRes> getNotificationsByCurrentAccount(Pageable pageable);

    NotificationRes markAsRead(Long notificationId);

    void markAllAsRead();

    void deleteNotification(Long notificationId);
}
