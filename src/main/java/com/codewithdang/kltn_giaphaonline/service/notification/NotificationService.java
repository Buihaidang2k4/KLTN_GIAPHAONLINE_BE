package com.codewithdang.kltn_giaphaonline.service.notification;

import com.codewithdang.kltn_giaphaonline.dto.request.NotificationReq;
import com.codewithdang.kltn_giaphaonline.dto.response.NotificationRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    NotificationRes createNotification(NotificationReq request);

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

    PageResponse<NotificationRes> getNotifications(Long recipientAccountId, Pageable pageable);

    PageResponse<NotificationRes> getUnreadNotifications(Long recipientAccountId, Pageable pageable);

    long countUnreadNotifications(Long recipientAccountId);

    NotificationRes markAsRead(Long notificationId, Long recipientAccountId);

    void markAllAsRead(Long recipientAccountId);

    void deleteNotification(Long notificationId, Long recipientAccountId);
}
