package com.codewithdang.kltn_giaphaonline.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants(level = AccessLevel.PRIVATE)
@Table(name = "password_reset_tokens")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "password_reset_id")
    Long passwordResetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    Account account;

    @Column(name = "otp_hash", length = 10)
    String otpHash;

    @Column(name = "requested_ip", length = 45)
    String requestedIp;

    @Column(name = "expires_at")
    Instant expiresAt;

    @Column(name = "used_at")
    Instant usedAt;

    @Column(name = "requested_at")
    Instant requestedAt;

    @Column(name = "is_success")
    Boolean isSuccess;
}
