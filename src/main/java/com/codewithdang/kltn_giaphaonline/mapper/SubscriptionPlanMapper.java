package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.SubscriptionPlanReq;
import com.codewithdang.kltn_giaphaonline.dto.response.SubscriptionPlanRes;
import com.codewithdang.kltn_giaphaonline.entity.SubscriptionPlan;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface SubscriptionPlanMapper {
    SubscriptionPlan toEntity(SubscriptionPlanReq request);

    SubscriptionPlanRes toRes(SubscriptionPlan plan);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updatePlanFromRequest(SubscriptionPlanReq request, @MappingTarget SubscriptionPlan plan);
}
