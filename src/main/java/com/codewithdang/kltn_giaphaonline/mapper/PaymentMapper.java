package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.response.PaymentRes;
import com.codewithdang.kltn_giaphaonline.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "accountId", source = "account.accountId")
    @Mapping(target = "subscriptionPlanId", source = "subscriptionPlan.subscriptionPlanId")
    @Mapping(target = "familyId", source = "family.familyId")
    @Mapping(target = "familySubscriptionId", source = "familySubscription.familySubscriptionId")
    PaymentRes toRes(Payment payment);
}
