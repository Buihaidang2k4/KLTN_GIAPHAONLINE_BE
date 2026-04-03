package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AccountRes {
    Long accountId;
    String email;
    String fullName;
    String avatarPath;
    String avatarUrl;
    String accountStatus;
    String lockReason;
    LocalDateTime lockedAt;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<String> roles;
}
