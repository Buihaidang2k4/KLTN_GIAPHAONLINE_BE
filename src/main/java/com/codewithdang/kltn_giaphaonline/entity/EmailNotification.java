package com.codewithdang.kltn_giaphaonline.entity;


import com.codewithdang.kltn_giaphaonline.enums.EmailNotificationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "email_notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    Account account;

    @Column(name = "recipient_email", nullable = false, length = 255)
    String recipientEmail;

    @Column(name = "subject", length = 255)
    String subject;

    @Column(name = "template_name", length = 50)
    String templateName;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", columnDefinition = "jsonb")
    String payload;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    EmailNotificationStatus status;

    @Column(name = "retry_count")
    Integer retryCount;

    @Lob
    @Column(name = "error_message")
    String errorMessage;

    @Column(name = "sent_at")
    Instant sentAt;

    @Column(name = "created_at")
    Instant createdAt;
}