package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InviteInvitationMemberRes {
    Long familyInvitationId;
    String familyName;
    String invitedEmail;
    String invitedBy;
    String invitationStatus;
    String inviteToken;
    Instant createdAt;
    Instant expiredAt;
}
