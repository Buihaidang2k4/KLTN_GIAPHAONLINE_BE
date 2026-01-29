package com.codewithdang.kltn_giaphaonline.entity;

import com.codewithdang.kltn_giaphaonline.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    Long accountId;

    @Column(nullable = false, unique = true)
    String email;

    @Column(name = "full_name", nullable = false)
    String fullName;

    @Column(name = "avatar_path")
    String avatarPath;

    @Transient
    String avatarUrl;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    AccountStatus accountStatus;

    @Column(name = "lock_reason", columnDefinition = "TEXT")
    String lockReason;

    @Column(name = "locked_at")
    LocalDateTime lockedAt;

    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    Set<AccountRole> accountRoles = new HashSet<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
