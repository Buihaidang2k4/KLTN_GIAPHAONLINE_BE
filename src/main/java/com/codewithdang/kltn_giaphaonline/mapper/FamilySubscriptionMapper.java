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
    @Mapping(target = "price", source = "subscriptionPlan.price")
    @Mapping(target = "maxPerson", source = "subscriptionPlan.maxPerson")
    @Mapping(target = "maxAdmin", source = "subscriptionPlan.maxAdmin")
    @Mapping(target = "maxStorageMb", source = "subscriptionPlan.maxStorageMb")
    @Mapping(target = "durationMonth", source = "subscriptionPlan.durationMonth")
    @Mapping(target = "familyId", source = "family.familyId")
    FamilySubscriptionRes toRes(FamilySubscription familySubscription);
}
