package com.codewithdang.kltn_giaphaonline.entity;

import com.codewithdang.kltn_giaphaonline.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_account_id", nullable = false)
    Account recipient; // Người nhận

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_account_id")
    Account sender;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    NotificationType type;

    @Column(name = "title", nullable = false, length = 255)
    String title;

    @Column(name = "content", columnDefinition = "text")
    String content;

    @Builder.Default
    @Column(name = "is_read", nullable = false)
    Boolean isRead = false;

    @Column(name = "reference_id")
    Long referenceId;

    @Column(name = "reference_type", length = 50)
    String referenceType;

    @Column(name = "action_url", length = 500)
    String actionUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    @Column(name = "read_at")
    Instant readAt;
}