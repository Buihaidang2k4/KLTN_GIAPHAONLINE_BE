package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.SubscriptionPlanReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.SubscriptionPlanRes;
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
@RequestMapping("${api.prefix}/subscription-plans")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Subscription Plan Management")
public class SubscriptionPlanController {

    SubscriptionPlanService subscriptionPlanService;

    @PostMapping
    public ResponseEntity<ApiResponse<SubscriptionPlanRes>> createPlan(
            @Valid @RequestBody SubscriptionPlanReq request) {
        return ResponseEntity.ok(ApiResponse.success(201, "CREATE_SUBSCRIPTION_PLAN_SUCCESS",
                subscriptionPlanService.createPlan(request)));
    }

    @PutMapping("/{planId}")
    public ResponseEntity<ApiResponse<SubscriptionPlanRes>> updatePlan(
            @PathVariable Long planId,
            @Valid @RequestBody SubscriptionPlanReq request) {
        return ResponseEntity.ok(ApiResponse.success(200, "UPDATE_SUBSCRIPTION_PLAN_SUCCESS",
                subscriptionPlanService.updatePlan(planId, request)));
    }

    @GetMapping("/{planId}")
    public ResponseEntity<ApiResponse<SubscriptionPlanRes>> getPlanById(@PathVariable Long planId) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_SUBSCRIPTION_PLAN_SUCCESS",
                subscriptionPlanService.getPlanById(planId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<SubscriptionPlanRes>>> getAllPlans(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Boolean isActive,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_ALL_SUBSCRIPTION_PLANS_SUCCESS",
                subscriptionPlanService.getAllPlans(keyword, isActive, pageable)));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<SubscriptionPlanRes>>> getActivePlans() {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_ACTIVE_SUBSCRIPTION_PLANS_SUCCESS",
                subscriptionPlanService.getActivePlans()));
    }

    @PatchMapping("/{planId}/toggle-active")
    public ResponseEntity<ApiResponse<SubscriptionPlanRes>> toggleActive(@PathVariable Long planId) {
        return ResponseEntity.ok(ApiResponse.success(200, "TOGGLE_SUBSCRIPTION_PLAN_SUCCESS",
                subscriptionPlanService.activePlan(planId)));
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<ApiResponse<Void>> deletePlan(@PathVariable Long planId) {
        subscriptionPlanService.deletePlan(planId);
        return ResponseEntity.ok(ApiResponse.success(200, "DELETE_SUBSCRIPTION_PLAN_SUCCESS", null));
    }
}
