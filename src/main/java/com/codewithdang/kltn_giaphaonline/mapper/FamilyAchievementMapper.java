package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyAchievementReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyAchievementReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyAchievementRes;
import com.codewithdang.kltn_giaphaonline.entity.FamilyAchievement;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface FamilyAchievementMapper {

    FamilyAchievement toEntity(FamilyAchievementReq req);

    @Mapping(target = "familyId", source = "family.familyId")
    @Mapping(target = "evidencePath", source = "evidencePath")
    FamilyAchievementRes toDto(FamilyAchievement entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateFamilyAchievementReq req, @MappingTarget FamilyAchievement entity);
}
