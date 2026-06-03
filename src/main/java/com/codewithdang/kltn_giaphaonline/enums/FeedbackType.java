package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedbackType implements BaseEnum {
    BUG_REPORT("Báo lỗi"),
    FEATURE_REQUEST("Đề xuất tính năng"),
    UI_FEEDBACK("Góp ý giao diện"),
    OTHER("Khác");

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}
