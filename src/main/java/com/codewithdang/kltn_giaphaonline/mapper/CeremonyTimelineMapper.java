package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelineReq;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyTimelineRes;
import com.codewithdang.kltn_giaphaonline.entity.CeremonyTimeline;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CeremonyTimelineMapper {
    CeremonyTimeline toEntity(CeremonyTimelineReq req);

    CeremonyTimelineRes toRes(CeremonyTimeline ceremony);
}
