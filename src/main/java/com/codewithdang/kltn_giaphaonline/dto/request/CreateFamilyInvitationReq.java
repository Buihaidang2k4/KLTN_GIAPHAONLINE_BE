package com.codewithdang.kltn_giaphaonline.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InviteMemberReq {
    Long familyId;
    String invitedEmail;
    String roleName;
    String message;
}
