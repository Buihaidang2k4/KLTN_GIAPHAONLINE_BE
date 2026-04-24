package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

@Data
public class CeremonyTimelineRes {
    Long timelineId;
    Long ceremonyId;
    Integer stepOrder;
    String stepName;
    String stepDescription;
    String stepGuideline;
    Set<CeremonyTimelinePreparationRes> timelinePreparations;
    Timestamp createdAt;
    Timestamp updatedAt;
}
