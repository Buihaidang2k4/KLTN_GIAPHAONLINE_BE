package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardSystemRes {

    // ==================== Tài khoản ====================
    Long totalAccounts;
    Long totalAccountsActive;
    Long totalAccountsLocked;

    // ==================== Gia phả ====================
    Long totalFamilies;
    Long totalFamiliesWithActiveSub;

    // ==================== Subscription ====================
    Long totalActiveSubscriptions;
    Long totalExpiredSubscriptions;

    // ==================== Doanh thu ====================
    BigDecimal revenueThisMonth;
    BigDecimal revenueLastMonth;
    BigDecimal revenueTotal;

    // ==================== Giao dịch ====================
    Long totalPayments;
    Long totalPaymentsSuccess;
    Long totalPaymentsFailed;

    // ==================== Tăng trưởng ====================
    List<MonthlyStatRes> accountGrowth;   // số account mới theo tháng
    List<MonthlyStatRes> revenueGrowth;   // doanh thu theo tháng
}
