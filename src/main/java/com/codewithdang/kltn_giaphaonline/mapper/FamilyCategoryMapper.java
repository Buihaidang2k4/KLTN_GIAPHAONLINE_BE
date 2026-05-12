package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyCategoryRes;
import com.codewithdang.kltn_giaphaonline.entity.FamilyCategory;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CeremonyTimelineMapper.class})
public interface FamilyCategoryMapper {
    FamilyCategory toEntity(FamilyCategoryReq req);

    @Mapping(target = "familyId", source = "family.familyId")
    @Mapping(target = "createdByAccountId", source = "owner.accountId")
    FamilyCategoryRes toRes(FamilyCategory familyCategory);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(FamilyCategoryReq req, @MappingTarget FamilyCategory entity);
}
