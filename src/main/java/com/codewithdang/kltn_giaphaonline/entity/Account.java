package com.codewithdang.kltn_giaphaonline.entity;

import com.codewithdang.kltn_giaphaonline.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    Long accountId;

    @Column(nullable = false, unique = true)
    String email;

    @Column(name = "full_name", nullable = false)
    String fullName;

    @Column(name = "phone_number", unique = true, length = 20)
    String phoneNumber;

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

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

    @Builder.Default
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    Set<AccountRole> accountRoles = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "owner")
    Set<Family> ownedFamilies = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "account")
    Set<FamilyMember> familyMemberships = new LinkedHashSet<>();
}
