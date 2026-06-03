package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.FeedbackHandleReq;
import com.codewithdang.kltn_giaphaonline.dto.request.FeedbackReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FeedbackRes;
import com.codewithdang.kltn_giaphaonline.entity.Feedback;
import org.mapstruct.*;
@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    Feedback toEntity(FeedbackReq req);

    @Mapping(target = "email", source = "account.email")
    FeedbackRes toRes(Feedback feedback);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(FeedbackHandleReq req, @MappingTarget Feedback entity);
}
