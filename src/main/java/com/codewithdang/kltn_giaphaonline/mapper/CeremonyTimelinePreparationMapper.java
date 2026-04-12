package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelinePreparationReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelinePreparationUpdateReq;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyTimelinePreparationRes;
import com.codewithdang.kltn_giaphaonline.entity.CeremonyTimelinePreparation;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CeremonyTimelinePreparationMapper {
    CeremonyTimelinePreparation toEntity(CeremonyTimelinePreparationReq timelinePreparationReq);

    @Mapping(target = "timelineId", source = "timeline.timelineId")
    CeremonyTimelinePreparationRes toRes(CeremonyTimelinePreparation timelinePreparation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CeremonyTimelinePreparationUpdateReq timelineUpdateReq, @MappingTarget CeremonyTimelinePreparation timelinePreparation);
}
