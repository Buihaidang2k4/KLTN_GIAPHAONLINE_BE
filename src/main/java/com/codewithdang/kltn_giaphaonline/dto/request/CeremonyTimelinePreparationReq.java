package com.codewithdang.kltn_giaphaonline.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CeremonyTimelinePreparationReq {
    Long timelineId;
    String itemName;
    String itemType;
    Integer quantity;
    String unit;
    String note;
    Boolean required;
}
