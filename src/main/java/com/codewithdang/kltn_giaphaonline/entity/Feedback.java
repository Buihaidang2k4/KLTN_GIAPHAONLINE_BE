package com.codewithdang.kltn_giaphaonline.entity;

import com.codewithdang.kltn_giaphaonline.enums.FeedbackStatus;
import com.codewithdang.kltn_giaphaonline.enums.FeedbackType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "feedbacks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long feedbackId;

    /**
     * Người gửi phản hồi
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    FeedbackType type;

    @Column(nullable = false, length = 255)
    String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    FeedbackStatus status;

    @Column(columnDefinition = "TEXT")
    String adminResponse;

    @CreationTimestamp
    @Column(nullable = false)
    LocalDateTime createdAt;

    LocalDateTime resolvedAt;
}