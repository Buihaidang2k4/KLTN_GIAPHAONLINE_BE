package com.codewithdang.kltn_giaphaonline.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

// verify account register
@Entity
@Table(
        name = "account_verification_tokens",
        indexes = {
                @Index(name = "idx_verify_account_id", columnList = "account_id"),
                @Index(name = "idx_verify_token", columnList = "token"),
                @Index(name = "idx_verify_expires_at", columnList = "expires_at"),
                @Index(name = "idx_verify_is_used", columnList = "is_used")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_id")
    Long verificationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    Account account;

    @Column(name = "token", nullable = false, unique = true, length = 255)
    String token;

    @Column(name = "otp_code", length = 10)
    String otpCode;

    @Column(name = "expires_at", nullable = false)
    Instant expiresAt;

    @Column(name = "verified_at")
    Instant verifiedAt;

    @Column(name = "is_used", nullable = false)
    Boolean isUsed;

    @Column(name = "requested_ip", length = 45)
    String requestedIp;

    @Column(name = "user_agent", length = 255)
    String userAgent;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    Instant createdAt;
}