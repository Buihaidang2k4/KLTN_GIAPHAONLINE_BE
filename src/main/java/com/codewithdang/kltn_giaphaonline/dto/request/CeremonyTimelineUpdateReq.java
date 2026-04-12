package com.codewithdang.kltn_giaphaonline.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CeremonyTimelineUpdateReq {
    Integer stepOrder;
    String stepName;
    String stepDescription;
    String stepGuideline;
}
