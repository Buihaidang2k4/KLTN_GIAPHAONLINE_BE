package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AchievementType implements BaseEnum {
    EDUCATION("Học vấn"),
    AWARD("Giải thưởng"),
    CERTIFICATE("Chứng chỉ"),
    TITLE("Danh hiệu");

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}