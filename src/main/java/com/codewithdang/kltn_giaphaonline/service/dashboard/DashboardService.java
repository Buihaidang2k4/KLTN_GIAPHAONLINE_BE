package com.codewithdang.kltn_giaphaonline.service.dashboard;

import com.codewithdang.kltn_giaphaonline.dto.response.DashboardRes;

public interface DashboardService {
    DashboardRes getDataDashboard(Long familyId);
}
