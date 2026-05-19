package com.codewithdang.kltn_giaphaonline.service.dashboard;

import com.codewithdang.kltn_giaphaonline.dto.response.DashboardRes;
import com.codewithdang.kltn_giaphaonline.dto.response.DashboardSystemRes;

public interface DashboardService {
    DashboardRes getDataDashboardUser(Long familyId);

    DashboardSystemRes getDataDashboardSystem();
}
