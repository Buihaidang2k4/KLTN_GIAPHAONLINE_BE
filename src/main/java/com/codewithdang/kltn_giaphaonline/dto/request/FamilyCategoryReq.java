package com.codewithdang.kltn_giaphaonline.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FamilyCategoryReq {
    @NotNull(message = "familyName not null")
    String familyName;
    String origin;
    String description;
    Boolean isPublic;
}
