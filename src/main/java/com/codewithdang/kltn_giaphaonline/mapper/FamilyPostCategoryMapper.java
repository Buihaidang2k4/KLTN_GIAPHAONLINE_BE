package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.PostCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyPostCategoryRes;
import com.codewithdang.kltn_giaphaonline.entity.FamilyPostCategory;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FamilyPostCategoryMapper {
    FamilyPostCategory toEntity(PostCategoryReq req);

    @Mapping(target = "familyId", source = "family.familyId")
    FamilyPostCategoryRes toDto(FamilyPostCategory familyPostCategory);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePostCategory(PostCategoryReq req, @MappingTarget FamilyPostCategory familyPostCategory);
}
