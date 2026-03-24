package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlanName implements BaseEnum {
    KHOI_DAU("Gói Khởi Đầu"),     // Gói miễn phí, giới hạn số thành viên
    DONG_TAM("Gói Đồng Tâm"),     // Gói cơ bản cho dòng họ nhỏ
    THINH_VUONG("Gói Thịnh Vượng"), // Gói cao cấp, đầy đủ tính năng
    BAN_SAC("Gói Bản Sắc");       // Gói đặc biệt (V.I.P) với các tính năng lưu trữ di sản

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}