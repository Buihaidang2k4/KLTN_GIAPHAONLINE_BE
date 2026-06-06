package com.codewithdang.kltn_giaphaonline.service.notification;

import com.codewithdang.kltn_giaphaonline.dto.request.NotificationReq;
import com.codewithdang.kltn_giaphaonline.dto.response.NotificationRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Notification;
import com.codewithdang.kltn_giaphaonline.entity.FamilyMember;
import com.codewithdang.kltn_giaphaonline.enums.FamilyMemberStatus;
import com.codewithdang.kltn_giaphaonline.enums.NotificationType;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.NotificationMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyMemberRepo;
import com.codewithdang.kltn_giaphaonline.repo.NotificationRepo;
import com.codewithdang.kltn_giaphaonline.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl implements NotificationService {

    NotificationRepo notificationRepo;
    NotificationMapper notificationMapper;
    AccountRepo accountRepo;
    FamilyMemberRepo familyMemberRepo;
    PageMapper pageMapper;
    SimpMessagingTemplate messagingTemplate;
    SecurityUtils securityUtils;

    @Override
    @Transactional
    public NotificationRes createNotification(NotificationReq request) {
        Account recipient = accountRepo.findById(request.getRecipientAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_EXISTED));

        Account sender = null;
        if (request.getSenderAccountId() != null)
            sender = accountRepo.findById(request.getSenderAccountId()).orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        Notification notification = Notification.builder()
                .recipient(recipient)
                .sender(sender)
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .isRead(false)
                .referenceId(request.getReferenceId())
                .referenceType(request.getReferenceType())
                .actionUrl(request.getActionUrl())
                .build();

        notification = notificationRepo.save(notification);

        // send realtime
        sendRealtimeNotification(notification);

        return notificationMapper.toRes(notification);
    }

    @Override
    @Transactional
    public NotificationRes createNotification(
            Long recipientAccountId,
            Long senderAccountId,
            NotificationType type,
            String title,
            String content,
            Long referenceId,
            String referenceType,
            String actionUrl
    ) {
        Account recipient = accountRepo.findById(recipientAccountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        Account sender = null;
        if (senderAccountId != null) {
            sender = accountRepo.findById(senderAccountId)
                    .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
        }

        Notification notification = Notification.builder()
                .recipient(recipient)
                .sender(sender)
                .type(type)
                .title(title)
                .content(content)
                .isRead(false)
                .referenceId(referenceId)
                .referenceType(referenceType)
                .actionUrl(actionUrl)
                .build();

        notification = notificationRepo.save(notification);
        sendRealtimeNotification(notification);
        return notificationMapper.toRes(notification);
    }


    @Override
    @Transactional
    public void createFamilyNotification(Long familyId, Long senderAccountId,
                                         NotificationType type, String title, String content,
                                         Long referenceId, String referenceType, String actionUrl) {
        List<FamilyMember> members = familyMemberRepo.findByFamily_FamilyIdAndStatus(familyId, FamilyMemberStatus.ACTIVE);
        if (members.isEmpty()) return;

        Account sender = (senderAccountId != null)
                ? accountRepo.findById(senderAccountId).orElse(null)
                : null;

        List<Notification> notifications = members.stream()
                .filter(m -> !m.getAccount().getAccountId().equals(senderAccountId))
                .map(m -> Notification.builder()
                        .recipient(m.getAccount())
                        .sender(sender)
                        .type(type)
                        .title(title)
                        .content(content)
                        .isRead(false)
                        .referenceId(referenceId)
                        .referenceType(referenceType)
                        .actionUrl(actionUrl)
                        .build())
                .toList();

        notificationRepo.saveAll(notifications);
        notifications.forEach(this::sendRealtimeNotification);
        log.info("Sent family notification to {} members in family {}", notifications.size(), familyId);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<NotificationRes> getNotificationsByCurrentAccount(Pageable pageable) {
        Account currentAccount = securityUtils.getCurrentAccount();
        Page<Notification> notificationPage = notificationRepo.findByRecipient_AccountIdOrderByCreatedAtDesc(currentAccount.getAccountId(), pageable);
        return pageMapper.toPageResponse(notificationPage, notificationMapper::toRes);
    }

    @Override
    @Transactional
    public NotificationRes markAsRead(Long notificationId) {

        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_EXISTED));

        if (Boolean.FALSE.equals(notification.getIsRead())) {
            notification.setIsRead(true);
            notification.setReadAt(Instant.now());
        }

        return notificationMapper.toRes(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead() {
        Account currentAccount = securityUtils.getCurrentAccount();
        notificationRepo.markAllAsRead(currentAccount.getAccountId(), Instant.now());
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_EXISTED));
        notificationRepo.delete(notification);
    }


    private void sendRealtimeNotification(Notification notification) {
        String recipientEmail = notification.getRecipient().getEmail();
        String recipientName = notification.getRecipient().getFullName();
        String senderUsername = notification.getSender() != null
                ? notification.getSender().getFullName()
                : null;

        NotificationRes res = NotificationRes.builder()
                .notificationId(notification.getNotificationId())
                .recipientAccountId(notification.getRecipient().getAccountId())
                .senderAccountId(notification.getSender() != null ? notification.getSender().getAccountId() : null)
                .senderName(senderUsername)
                .recipientName(recipientName)
                .type(notification.getType())
                .title(notification.getTitle())
                .isRead(notification.getIsRead())
                .content(notification.getContent())
                .referenceId(notification.getReferenceId())
                .referenceType(notification.getReferenceType())
                .actionUrl(notification.getActionUrl())
                .build();
        messagingTemplate.convertAndSendToUser(recipientEmail, "/queue/notifications", res);
        log.info("PUSHHHH _ NOTIFICATION _ SUCCESS to user: {}", recipientEmail);
    }
}
