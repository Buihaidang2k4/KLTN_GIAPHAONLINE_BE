package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FamilyMemberStatus implements BaseEnum {
    ACTIVE("Đang tham gia"),    // Thành viên bình thường, có quyền xem/sửa (tùy role)
    REMOVED("Đã bị xóa"),      // Admin dòng họ chủ động kích ra
    LEFT("Đã rời khỏi"),       // Thành viên tự nguyện xin ra khỏi nhóm dòng họ này
    BLOCKED("Đã bị chặn");     // Bị chặn vĩnh viễn không cho vào lại dòng họ này

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}