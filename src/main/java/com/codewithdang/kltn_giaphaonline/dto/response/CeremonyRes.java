package com.codewithdang.kltn_giaphaonline.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.Set;

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
    Set<CeremonyTimelineRes> timelines;
    Timestamp createdAt;
    Timestamp updatedAt;
}
