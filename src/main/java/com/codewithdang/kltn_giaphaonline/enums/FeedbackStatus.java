package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedbackStatus implements BaseEnum {
    PENDING("Chờ xử lý"),
    PROCESSING("Đang xử lý"),
    RESOLVED("Đã xử lý"),
    REJECTED("Từ chối");

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}
