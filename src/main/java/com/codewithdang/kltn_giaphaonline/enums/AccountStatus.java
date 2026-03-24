package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountStatus implements BaseEnum {
    ACTIVE("Đang hoạt động"),
    LOCKED("Đã bị khóa"),
    DELETED("Đã xóa"),
    PENDING("Chờ xác nhận");

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}
