package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelineReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelineUpdateReq;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyTimelineRes;
import com.codewithdang.kltn_giaphaonline.entity.CeremonyTimeline;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CeremonyTimelineMapper {
    CeremonyTimeline toEntity(CeremonyTimelineReq req);

    CeremonyTimelineRes toRes(CeremonyTimeline ceremony);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CeremonyTimelineUpdateReq timelineUpdateReq, @MappingTarget CeremonyTimeline ceremonyTimeline);
}
