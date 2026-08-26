package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.DashboardRes;
import com.codewithdang.kltn_giaphaonline.dto.response.DashboardSystemRes;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
import com.codewithdang.kltn_giaphaonline.service.dashboard.DashboardService;
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
@RequestMapping(ApiPath.Dashboard.BASE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Dashboard Management")
public class DashboardController {

    DashboardService dashboardService;

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Dashboard.FAMILY)
    public ResponseEntity<ApiResponse<DashboardRes>> getDashboard(@PathVariable Long familyId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Dashboard.GET_DASHBOARD_SUCCESS,
                dashboardService.getDataDashboardUser(familyId)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Dashboard.SYSTEM)
    public ResponseEntity<ApiResponse<DashboardSystemRes>> getDashboardAdmin() {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Dashboard.GET_DASHBOARD_SYSTEM_SUCCESS,
                dashboardService.getDataDashboardSystem()));
    }
}
