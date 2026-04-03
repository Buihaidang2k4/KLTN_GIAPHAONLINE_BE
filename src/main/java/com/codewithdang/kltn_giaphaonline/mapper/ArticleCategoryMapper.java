package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateArticleCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateArticleCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ArticleCategoryRes;
import com.codewithdang.kltn_giaphaonline.entity.ArticleCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ArticleCategoryMapper {
    ArticleCategory toEntity(CreateArticleCategoryReq req);

    ArticleCategoryRes toRes(ArticleCategory entity);

    void updateEntity(@MappingTarget ArticleCategory entity, UpdateArticleCategoryReq req);
}
