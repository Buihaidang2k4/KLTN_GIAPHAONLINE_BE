package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RelationType implements BaseEnum {
    // Quan hệ hôn nhân (Ngang)
    SPOUSE("Vợ/Chồng"),
    DIVORCED("Đã ly hôn"),
    CONCUBINE("Vợ lẽ/Vợ thứ"),

    // Quan hệ nuôi dưỡng (Nếu bạn muốn làm chi tiết)
    ADOPTED_CHILD("Con nuôi"),
    STEP_PARENT("Cha/Mẹ kế");

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}