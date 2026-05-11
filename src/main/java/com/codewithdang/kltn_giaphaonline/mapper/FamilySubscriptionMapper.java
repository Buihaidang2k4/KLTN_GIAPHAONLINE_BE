package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.response.FamilySubscriptionRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PaymentRes;
import com.codewithdang.kltn_giaphaonline.entity.FamilySubscription;
import com.codewithdang.kltn_giaphaonline.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FamilySubscriptionMapper {

    @Mapping(target = "createdByAccountId", source = "createdByAccount.accountId")
    @Mapping(target = "subscriptionPlanId", source = "subscriptionPlan.subscriptionPlanId")
    @Mapping(target = "planName", source = "subscriptionPlan.namePlan")
    @Mapping(target = "planCode", source = "subscriptionPlan.code")
    @Mapping(target = "familyId", source = "family.familyId")
    FamilySubscriptionRes toRes(FamilySubscription familySubscription);
}
