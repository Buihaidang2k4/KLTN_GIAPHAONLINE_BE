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

    @Column(name = "invited_email", nullable = false, length = 255)
    String invitedEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_account_id")
    Account invitedAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_name")
    Role role;

    @Column(name = "invite_token", nullable = false, unique = true, length = 255)
    String inviteToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    FamilyInvitationStatus status;

    @Lob
    @Column(name = "message")
    String message;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invited_by_account_id", nullable = false)
    Account invitedByAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accepted_by_account_id")
    Account acceptedByAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "declined_by_account_id")
    Account declinedByAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canceled_by_account_id")
    Account canceledByAccount;

    @Column(name = "expired_at")
    Instant expiredAt;

    @Column(name = "accepted_at")
    Instant acceptedAt;

    @Column(name = "declined_at")
    Instant declinedAt;

    @Column(name = "canceled_at")
    Instant canceledAt;

    @Column(name = "last_sent_at")
    Instant lastSentAt;

    @Column(name = "send_count")
    Integer sendCount;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Instant updatedAt;
}