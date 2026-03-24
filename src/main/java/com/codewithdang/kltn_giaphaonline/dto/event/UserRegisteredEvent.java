package com.codewithdang.kltn_giaphaonline.dto.event;

public record UserRegisteredEvent(
        Long userId,
        String email,
        String fullName,
        String verificationToken
) {
}
