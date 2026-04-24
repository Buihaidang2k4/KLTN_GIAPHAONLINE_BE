package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.PostCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyPostCategoryRes;
import com.codewithdang.kltn_giaphaonline.entity.FamilyPostCategory;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface FamilyPostCategoryMapper {
    FamilyPostCategory toEntity(PostCategoryReq req);

    FamilyPostCategoryRes toDto(FamilyPostCategory familyPostCategory);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePostCategory(PostCategoryReq req, @MappingTarget FamilyPostCategory familyPostCategory);
}
