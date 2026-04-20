package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CeremonyTimelinePreparationRes {
    Long preparationId;
    Long timelineId;
    String itemName;
    String itemType;
    Integer quantity;
    String unit;
    String note;
    Boolean required;
    Timestamp createdAt;
    Timestamp updatedAt;
}
