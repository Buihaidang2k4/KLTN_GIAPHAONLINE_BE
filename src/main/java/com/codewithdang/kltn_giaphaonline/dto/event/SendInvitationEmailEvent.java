package com.codewithdang.kltn_giaphaonline.dto.event;

public record SendInvitationEmailEvent(
        String invitedEmail,
        String invitedToken
) {
}
