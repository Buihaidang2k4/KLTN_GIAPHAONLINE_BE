package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FamilyInvitationStatus implements BaseEnum {
    PENDING("Đang chờ"),      // Lời mời đã gửi, chờ người nhận phản hồi
    ACCEPTED("Đã chấp nhận"), // Người nhận đã đồng ý tham gia
    DECLINED("Đã từ chối"),   // Người nhận không muốn tham gia
    EXPIRED("Hết hạn"),      // Lời mời quá lâu không phản hồi (ví dụ sau 7 ngày)
    CANCELED("Đã hủy");      // Người gửi chủ động thu hồi lời mời

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}