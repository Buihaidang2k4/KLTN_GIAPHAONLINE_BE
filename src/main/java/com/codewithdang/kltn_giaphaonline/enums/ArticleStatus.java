package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public enum ArticleStatus implements BaseEnum {
    DRAFT("Bản nháp"),
    PUBLISHED("Đã xuất bản"),
    ARCHIVED("Lưu trữ"),
    HIDDEN("Bị ẩn");

    private final String label;

    @Override
    public String getLabel() {
        return "";
    }

}
