package com.codewithdang.kltn_giaphaonline.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "revoked_tokens")
public class RevokedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    Long refreshTokenId;

    @Column(name = "token_hash", nullable = false, unique = true, length = 255)
    String tokenHash;

    @Column(name = "revoked_at")
    Instant revokedAt;

    @Column(name = "device_info", length = 255)
    String deviceInfo;

    @Column(name = "ip_address", length = 45)
    String ipAddress;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    Instant expiresAt;
}
