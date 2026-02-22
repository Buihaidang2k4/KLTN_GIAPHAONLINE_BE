package com.codewithdang.kltn_giaphaonline.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record AccountRes(
        Long accountId,

        String email,

        String fullName,

        String avatarPath,

        String avatarUrl,

        String accountStatus,

        String lockReason,

        LocalDateTime lockedAt,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        List<String> roles
) {
}
