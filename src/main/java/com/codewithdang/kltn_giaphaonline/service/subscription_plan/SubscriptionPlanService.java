package com.codewithdang.kltn_giaphaonline.service.subscription_plan;

import com.codewithdang.kltn_giaphaonline.dto.request.SubscriptionPlanReq;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.SubscriptionPlanRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubscriptionPlanService {
    SubscriptionPlanRes activePlan(Long planId);

    SubscriptionPlanRes createPlan(SubscriptionPlanReq request);

    SubscriptionPlanRes createPlanDefault();

    SubscriptionPlanRes updatePlan(Long planId, SubscriptionPlanReq request);

    SubscriptionPlanRes getPlanById(Long planId);

    PageResponse<SubscriptionPlanRes> getAllPlans(String keyword, Boolean isActive, Pageable pageable);

    List<SubscriptionPlanRes> getActivePlans();

    void deletePlan(Long planId);
}
