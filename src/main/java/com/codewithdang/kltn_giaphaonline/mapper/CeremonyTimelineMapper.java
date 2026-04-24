package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelineReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelineUpdateReq;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyTimelineRes;
import com.codewithdang.kltn_giaphaonline.entity.CeremonyTimeline;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CeremonyTimelinePreparationMapper.class})
public interface CeremonyTimelineMapper {
    CeremonyTimeline toEntity(CeremonyTimelineReq req);

    @Mapping(target = "ceremonyId", source = "ceremony.ceremonyId")
    @Mapping(target = "timelineId", source = "timelineId")
    @Mapping(target = "timelinePreparations", source = "preparations")
    CeremonyTimelineRes toRes(CeremonyTimeline ceremony);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CeremonyTimelineUpdateReq timelineUpdateReq, @MappingTarget CeremonyTimeline ceremonyTimeline);
}
