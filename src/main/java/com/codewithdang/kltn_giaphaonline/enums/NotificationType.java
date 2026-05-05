package com.codewithdang.kltn_giaphaonline.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType implements BaseEnum {
    FAMILY_INVITATION("FAMILY_INVITATION"),
    FAMILY_MEMBER_ADDED("FAMILY_MEMBER_ADDED"),
    FAMILY_MEMBER_REMOVED("FAMILY_MEMBER_REMOVED"),
    FAMILY_ROLE_CHANGED("FAMILY_ROLE_CHANGED"),
    SYSTEM("SYSTEM"),
    ;


    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}
