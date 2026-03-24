package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailNotificationStatus implements BaseEnum {
    PENDING("Đang chờ gửi"),
    SENDING("Đang trong quá trình gửi"),
    SENT("Đã gửi thành công"),
    FAILED("Gửi thất bại"),
    RETRYING("Đang thử lại");

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}