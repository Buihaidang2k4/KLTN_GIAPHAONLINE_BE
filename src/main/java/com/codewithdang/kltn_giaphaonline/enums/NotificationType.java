package com.codewithdang.kltn_giaphaonline.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType implements BaseEnum {
    FAMILY_INVITATION("Thông báo khi nhận được lời mời"),
    FAMILY_MEMBER_ADDED_OR_REJECT("Thông báo khi người dùng chấp nhận lời mời tham gia gia phả"),
    FAMILY_MEMBER_REMOVED("Thông báo khi người dùng bị xóa khỏi gia phả"),
    FAMILY_MEMBER_ROLE_CHANGED("Thông báo khi người dùng được thay đổi quyền trong gia phả"),
    FAMILY_EVENT_UPCOMING("Thông báo sự kiện sắp tới "),
    FEEDBACK_USER("Thông báo feedback của bạn đã được phản hồi "),
    SUBSCRIPTION_EXPIRES("Thông báo gói dịch vụ sắp hết hạn"),
    SUBSCRIPTION_EXPIRED("Thông báo gói dịch vụ đã hết hạn"),
    FEEDBACK_ADMIN("Nhận phản hồi từ người dùng"),
    ;


    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}
