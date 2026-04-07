package com.codewithdang.kltn_giaphaonline.dto.request.email;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailInvitationAccount extends EmailBase {
    String senderFullName;
    String familyName;
    String invitationToken;
    Instant expiryHours;
    String personalMessage;
}