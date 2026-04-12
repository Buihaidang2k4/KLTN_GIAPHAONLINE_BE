package com.codewithdang.kltn_giaphaonline.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CeremonyTimelinePreparationUpdateReq {
    String itemName;
    String itemType;
    Integer quantity;
    String unit;
    String note;
    Boolean required;
}
