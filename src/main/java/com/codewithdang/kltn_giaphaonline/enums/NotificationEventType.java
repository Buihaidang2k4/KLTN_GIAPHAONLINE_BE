package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType implements BaseEnum {
    NONE("Không thông báo"),
    DAY_1("Trước 1 ngày"),
    DAY_3("Trước 3 ngày"),
    DAY_7("Trước 7 ngày"),
    DAY_15("Trước 15 ngày"),
    MONTH_1("Trước 1 tháng");

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}