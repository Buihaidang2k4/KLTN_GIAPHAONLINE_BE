package com.codewithdang.kltn_giaphaonline.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumReq {
    @NotNull(message = "title may not be null")
    String title;
    String description;
}
