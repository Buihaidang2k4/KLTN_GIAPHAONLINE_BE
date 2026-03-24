package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FamilyPostType implements BaseEnum {
    ANNOUNCEMENT("Thông báo"),  // Tin khẩn, họp họ, đóng góp quỹ...
    EVENT("Sự kiện"),           // Giỗ tổ, đám cưới, mừng thọ...
    HISTORY("Lịch sử"),         // Sự tích dòng họ, tiểu sử cụ tổ...
    ACHIEVEMENT("Vinh danh"),   // Con cháu đỗ đạt, thành tích dòng họ...
    OTHERS("Khác");            // Các tin tức linh tinh khác

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}