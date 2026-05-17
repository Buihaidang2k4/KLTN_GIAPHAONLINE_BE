package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class FamilyCategoryRes {
    Long familyCategoryId;
    Long createdByAccountId;
    Long familyId;
    String familyName;
    String origin;
    Long totalPerson;
    String description;
    Boolean isPublic;
    Instant createdAt;
    Instant updatedAt;
}
