package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilySubscriptionCheckQuotaRes;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilySubscriptionRes;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
import com.codewithdang.kltn_giaphaonline.service.family_subscription.FamilySubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPath.FamilySubscription.BASE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Family Subscription Management")
public class FamilySubscriptionController {

    FamilySubscriptionService familySubscriptionService;

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.FamilySubscription.BY_FAMILY)
    public ResponseEntity<ApiResponse<FamilySubscriptionRes>> getByFamilyId(@PathVariable Long familyId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilySubscription.GET_FAMILY_SUBSCRIPTION_SUCCESS, familySubscriptionService.getByFamilyId(familyId)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.FamilySubscription.USAGE_STORAGE)
    public ResponseEntity<ApiResponse<FamilySubscriptionCheckQuotaRes>> getByStorage(@PathVariable Long familyId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilySubscription.GET_FAMILY_SUBSCRIPTION_SUCCESS, familySubscriptionService.getFamilySubByQuotaUsage(familyId)));
    }

}
