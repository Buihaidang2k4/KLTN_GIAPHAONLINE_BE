package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RegisterRes {
    Long accountId;
    String email;
    String phoneNumber;
    String fullName;
    String avatarPath;
    String avatarUrl;
    String accountStatus;
    String lockReason;
    LocalDateTime lockedAt;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
