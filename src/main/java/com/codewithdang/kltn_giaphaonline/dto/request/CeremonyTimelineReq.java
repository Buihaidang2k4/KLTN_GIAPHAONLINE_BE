package com.codewithdang.kltn_giaphaonline.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CeremonyTimelineReq {
    @NotNull(message = "CEREMONY_ID_REQUIRED")
    Long ceremonyId;

    @NotBlank(message = "STEP_NAME_REQUIRED")
    @Size(max = 255, message = "STEP_NAME_TOO_LONG")
    String stepName;

    @Size(max = 500, message = "DESCRIPTION_TOO_LONG")
    String stepDescription;

    String stepGuideline;
}
