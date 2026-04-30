package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RepeatType implements BaseEnum {
    NONE("Không lặp lại"),
    YEARLY("Hàng năm");
//    MONTHLY("Hàng tháng"),
//    WEEKLY("Hàng tuần"),
//    DAILY("Hàng ngày");


    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}