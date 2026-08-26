package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.SubscriptionPlanReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.SubscriptionPlanRes;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
import com.codewithdang.kltn_giaphaonline.service.subscription_plan.SubscriptionPlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPath.SubscriptionPlan.BASE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Subscription Plan Management")
public class SubscriptionPlanController {

    SubscriptionPlanService subscriptionPlanService;

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping
    public ResponseEntity<ApiResponse<SubscriptionPlanRes>> createPlan(
            @Valid @RequestBody SubscriptionPlanReq request) {
        return ResponseEntity.ok(ApiResponse.success(201, MessageConstantsVi.SubscriptionPlan.CREATE_SUBSCRIPTION_PLAN_SUCCESS,
                subscriptionPlanService.createPlan(request)));
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PutMapping(ApiPath.SubscriptionPlan.BY_ID)
    public ResponseEntity<ApiResponse<SubscriptionPlanRes>> updatePlan(
            @PathVariable Long planId,
            @Valid @RequestBody SubscriptionPlanReq request) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.SubscriptionPlan.UPDATE_SUBSCRIPTION_PLAN_SUCCESS,
                subscriptionPlanService.updatePlan(planId, request)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.SubscriptionPlan.BY_ID)
    public ResponseEntity<ApiResponse<SubscriptionPlanRes>> getPlanById(@PathVariable Long planId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.SubscriptionPlan.GET_SUBSCRIPTION_PLAN_SUCCESS,
                subscriptionPlanService.getPlanById(planId)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<SubscriptionPlanRes>>> getAllPlans(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Boolean isActive,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.SubscriptionPlan.GET_ALL_SUBSCRIPTION_PLANS_SUCCESS,
                subscriptionPlanService.getAllPlans(keyword, isActive, pageable)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.SubscriptionPlan.ACTIVE)
    public ResponseEntity<ApiResponse<List<SubscriptionPlanRes>>> getActivePlans() {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.SubscriptionPlan.GET_ACTIVE_SUBSCRIPTION_PLANS_SUCCESS,
                subscriptionPlanService.getActivePlans()));
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PatchMapping(ApiPath.SubscriptionPlan.TOGGLE_ACTIVE)
    public ResponseEntity<ApiResponse<SubscriptionPlanRes>> toggleActive(@PathVariable Long planId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.SubscriptionPlan.TOGGLE_SUBSCRIPTION_PLAN_SUCCESS,
                subscriptionPlanService.activePlan(planId)));
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.SubscriptionPlan.BY_ID)
    public ResponseEntity<ApiResponse<Void>> deletePlan(@PathVariable Long planId) {
        subscriptionPlanService.deletePlan(planId);
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.SubscriptionPlan.DELETE_SUBSCRIPTION_PLAN_SUCCESS, null));
    }
}
