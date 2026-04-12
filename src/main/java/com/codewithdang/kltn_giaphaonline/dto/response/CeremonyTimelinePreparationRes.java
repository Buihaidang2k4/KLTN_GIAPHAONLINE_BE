package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.Data;

@Data
public class CeremonyTimelinePreparationRes {
    Long timelineId;
    String itemName;
    String itemType;
    Integer quantity;
    String unit;
    String note;
    Boolean required;
}
