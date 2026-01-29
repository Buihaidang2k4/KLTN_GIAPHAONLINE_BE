package com.codewithdang.kltn_giaphaonline.dto.response;

import java.time.LocalDateTime;

public record AccountRes(
        Long accountId,

        String email,

        String fullName,

        String avatarPath,

        String avatarUrl,

        String passwordHash,

        String accountStatus,

        String lockReason,

        LocalDateTime lockedAt,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
