package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleScopeType implements BaseEnum {
    SYSTEM("Toàn hệ thống"), // Các vai trò như Super Admin, Mod, Support
    FAMILY("Trong dòng họ");  // Các vai trò như Trưởng tộc, Thư ký, Thành viên

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}