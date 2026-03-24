package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LifeStatus implements BaseEnum {
    ALIVE("Còn sống"),
    DECEASED("Đã mất"),
    UNKNOWN("Không rõ"); // Dùng cho những cụ tổ đời xa xưa không có thông tin cụ thể

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}