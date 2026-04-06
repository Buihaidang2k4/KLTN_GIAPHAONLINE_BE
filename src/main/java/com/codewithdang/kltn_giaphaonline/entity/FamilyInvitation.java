package com.codewithdang.kltn_giaphaonline.entity;

import com.codewithdang.kltn_giaphaonline.enums.FamilyInvitationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "family_invitations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FamilyInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_invitation_id")
    Long familyInvitationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "family_id", nullable = false)
    Family family;

    @Column(name = "invited_email", nullable = false)
    String invitedEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_name")
    Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_account_id")
    Account invitedAccount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invited_by_account_id", nullable = false)
    Account invitedByAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    FamilyInvitationStatus invitationStatus;

    @Column(name = "invite_token", unique = true)
    String inviteToken;

    @Column(name = "message")
    String message;

    // Gom các mốc thời gian lại
    @Column(name = "expired_at")
    Instant expiredAt;

    @Column(name = "handled_at")
    Instant handledAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Instant updatedAt;
}