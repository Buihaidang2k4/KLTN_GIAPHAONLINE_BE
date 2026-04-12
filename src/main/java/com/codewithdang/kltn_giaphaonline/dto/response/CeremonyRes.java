package com.codewithdang.kltn_giaphaonline.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CeremonyRes {
    Long ceremonyId;
    Long familyId;
    String ceremonyType;
    String ceremonyName;
    String description;
    Timestamp createdAt;
    Timestamp updatedAt;
}
