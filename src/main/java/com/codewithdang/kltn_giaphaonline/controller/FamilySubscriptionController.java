package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilySubscriptionCheckQuotaRes;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilySubscriptionRes;
import com.codewithdang.kltn_giaphaonline.service.family_subscription.FamilySubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/family-subscriptions")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Family Subscription")
public class FamilySubscriptionController {
    FamilySubscriptionService familySubscriptionService;

    @GetMapping("/family/{familyId}")
    public ResponseEntity<ApiResponse<FamilySubscriptionRes>> getByFamilyId(@PathVariable Long familyId) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_FAMILY_SUBSCRIPTION_SUCCESS", familySubscriptionService.getByFamilyId(familyId)));
    }

    @GetMapping("/family/{familyId}/usageStorage")
    public ResponseEntity<ApiResponse<FamilySubscriptionCheckQuotaRes>> getByStorage(@PathVariable Long familyId) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_FAMILY_SUBSCRIPTION_SUCCESS", familySubscriptionService.getFamilySubByQuotaUsage(familyId)));
    }

}
