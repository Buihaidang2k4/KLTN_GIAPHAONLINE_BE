package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FamilyEventStatus implements BaseEnum {
    ACTIVE("Đang hoạt động"),
    INACTIVE("Tạm ẩn");

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}