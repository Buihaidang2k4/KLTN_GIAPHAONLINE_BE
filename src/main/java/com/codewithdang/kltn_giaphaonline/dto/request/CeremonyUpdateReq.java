package com.codewithdang.kltn_giaphaonline.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CeremonyUpdateReq {
    @NotBlank(message = "CEREMONY_TYPE_REQUIRED")
    String ceremonyType;

    @NotBlank(message = "CEREMONY_NAME_REQUIRED")
    @Size(min = 3, max = 255, message = "CEREMONY_NAME_INVALID_LENGTH")
    String ceremonyName;

    @Size(max = 1000, message = "DESCRIPTION_TOO_LONG")
    String description;
}
