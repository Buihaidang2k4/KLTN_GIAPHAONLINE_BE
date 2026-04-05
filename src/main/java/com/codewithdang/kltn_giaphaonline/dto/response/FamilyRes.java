package com.codewithdang.kltn_giaphaonline.dto.response;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FamilyRes {
    Long familyId;
    String slug;
    String familyName;
    String description;
    Instant createdAt;
    Instant updatedAt;
}