package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CeremonyTimelineRes {
    Long timelineId;
    Long ceremonyId;
    Integer stepOrder;
    String stepName;
    String stepDescription;
    String stepGuideline;
    Timestamp createdAt;
    Timestamp updatedAt;
}
