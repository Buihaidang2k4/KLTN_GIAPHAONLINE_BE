package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus implements BaseEnum {
    PENDING("Đang xử lý"),      // Giao dịch vừa khởi tạo, chờ người dùng nhập OTP/Quét mã
    SUCCESS("Thành công"),     // Đã nhận được tiền và xác thực từ Gateway (VNPAY)
    FAILED("Thất bại"),        // Giao dịch bị lỗi hoặc người dùng hủy thanh toán
    EXPIRED("Hết hạn"),        // Sau 15-30 phút người dùng không thanh toán
    REFUNDED("Đã hoàn tiền");  // Dùng cho trường hợp cần trả lại tiền cho dòng họ

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}