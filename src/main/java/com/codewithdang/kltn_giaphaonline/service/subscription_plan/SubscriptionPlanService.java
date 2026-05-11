package com.codewithdang.kltn_giaphaonline.service.subscription_plan;

import com.codewithdang.kltn_giaphaonline.dto.request.SubscriptionPlanReq;
import com.codewithdang.kltn_giaphaonline.dto.response.SubscriptionPlanRes;

import java.util.List;

public interface SubscriptionPlanService {
    SubscriptionPlanRes createPlan(SubscriptionPlanReq request);

    SubscriptionPlanRes createPlanDefault();

    SubscriptionPlanRes updatePlan(Long planId, SubscriptionPlanReq request);

    SubscriptionPlanRes getPlanById(Long planId);

    List<SubscriptionPlanRes> getAllPlans();

    List<SubscriptionPlanRes> getActivePlans();

    void deletePlan(Long planId);
}
