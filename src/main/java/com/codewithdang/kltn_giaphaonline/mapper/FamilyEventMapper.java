package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyEventRes;
import com.codewithdang.kltn_giaphaonline.entity.FamilyEvent;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface FamilyEventMapper {
    FamilyEvent toEntity(FamilyEventReq req);

    FamilyEventRes toDto(FamilyEvent entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEvent(UpdateFamilyEventReq req, @MappingTarget FamilyEvent entity);
}
