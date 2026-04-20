package com.codewithdang.kltn_giaphaonline.dto.response;

import com.codewithdang.kltn_giaphaonline.enums.FamilyMemberStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class FamilyMemberRes {
    Long familyId;
    Long accountId;
    String fullName;
    String email;
    String roleName;
    FamilyMemberStatus status;
    Instant joinedAt;
    Instant removedAt;
    Instant createdAt;
    Instant updatedAt;
}
