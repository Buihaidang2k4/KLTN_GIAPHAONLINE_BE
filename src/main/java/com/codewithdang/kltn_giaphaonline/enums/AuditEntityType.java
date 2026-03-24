package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuditEntityType implements BaseEnum {
    ACCOUNT("Tài khoản"),
    FAMILY("Dòng họ"),
    PERSON("Thành viên cây gia phả"),
    ROLE("Vai trò"),
    PERMISSION("Quyền hạn"),
    PAYMENT("Giao dịch thanh toán"),
    SUBSCRIPTION("Gói dịch vụ"),
    ARTICLE("Bài viết"),
    GENEALOGY_TREE("Cây gia phả");

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}