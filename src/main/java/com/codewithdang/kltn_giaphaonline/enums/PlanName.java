package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlanName implements BaseEnum {
    FREE("Gói Khởi Đầu"),     // Gói miễn phí, giới hạn số thành viên
    BASIC("Gói Đồng Tâm"),     // Gói cơ bản cho dòng họ nhỏ
    PREMIUM("Gói Thịnh Vượng"),
    PRO("Gói Phát Triển"),       // nhiều dung lượng hơn, nhiều tính năng hơn
    ENTERPRISE("Gói Di Sản");    // gói cao nhất cho dòng họ lớn// Gói cao cấp, đầy đủ tính năng

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}