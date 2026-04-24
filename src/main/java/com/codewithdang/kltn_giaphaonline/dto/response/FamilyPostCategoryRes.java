package com.codewithdang.kltn_giaphaonline.dto.response;

import java.time.Instant;

public record FamilyPostCategoryRes(
        Long categoryId,
        Long familyId,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
}
