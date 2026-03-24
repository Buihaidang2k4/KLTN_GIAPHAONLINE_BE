package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FamilyPostStatus implements BaseEnum {
    DRAFT("Bản nháp"),         // Đang soạn thảo, chỉ người viết mới thấy
    PUBLISHED("Đã xuất bản"),  // Hiển thị công khai cho mọi thành viên trong họ
    ARCHIVED("Lưu trữ"),      // Tin cũ, ẩn khỏi trang chính nhưng vẫn giữ trong kho
    HIDDEN("Bị ẩn");          // Quản trị viên ẩn đi do nội dung không phù hợp

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}