package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

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

    public static Optional<RoleScopeType> fromString(String scope) {
        if (scope == null) return Optional.empty();
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(scope.trim()))
                .findFirst();
    }
}