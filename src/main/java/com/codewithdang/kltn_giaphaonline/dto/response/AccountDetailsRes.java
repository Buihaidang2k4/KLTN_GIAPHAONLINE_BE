package com.codewithdang.kltn_giaphaonline.dto.response;

import com.codewithdang.kltn_giaphaonline.entity.Family;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class AccountDetailsRes {
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
    Set<RoleRes> roles;
    Set<FamilyRes> families;
}
