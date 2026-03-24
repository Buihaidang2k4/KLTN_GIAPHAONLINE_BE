package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubscriptionStatus implements BaseEnum {
    ACTIVE("Đang hoạt động"),    // Gói cước còn hạn, mọi tính năng mở khóa
    EXPIRED("Đã hết hạn"),      // Hết hạn sử dụng, chuyển về chế độ Read-only hoặc giới hạn
    CANCELED("Đã hủy"),         // Người dùng chủ động hủy gói (ngừng gia hạn)
    PENDING("Chờ thanh toán"),  // Giao dịch nâng cấp đang được xử lý
    GRACE_PERIOD("Trong thời gian chờ"); // (Gợi ý) 3-5 ngày sau khi hết hạn để nhắc gia hạn

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}