package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.DashboardRes;
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
@RequestMapping("${api.prefix}/dashboard")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Dashboard")
public class DashboardController {

    DashboardService dashboardService;

    @GetMapping("/family/{familyId}")
    public ResponseEntity<ApiResponse<DashboardRes>> getDataDashboard(@PathVariable Long familyId) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_DASHBOARD_SUCCESS",
                dashboardService.getDataDashboard(familyId)));
    }
}
