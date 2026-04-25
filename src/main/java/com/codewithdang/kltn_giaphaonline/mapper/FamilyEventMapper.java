package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyEventRes;
import com.codewithdang.kltn_giaphaonline.entity.FamilyEvent;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FamilyEventMapper {
    FamilyEvent toEntity(FamilyEventReq req);

    @Mapping(target = "createdByAccountId", source = "createdByAccount.accountId")
    @Mapping(target = "familyId", source = "family.familyId")
    FamilyEventRes toDto(FamilyEvent entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEvent(UpdateFamilyEventReq req, @MappingTarget FamilyEvent entity);
}
