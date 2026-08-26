package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.NotificationRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
import com.codewithdang.kltn_giaphaonline.service.notification.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPath.Notification.BASE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    NotificationService notificationService;

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping
    public ApiResponse<PageResponse<NotificationRes>> getMyNotifications(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ApiResponse.success(200, MessageConstantsVi.Notification.GET_NOTIFICATIONS_SUCCESS,
                notificationService.getNotificationsByCurrentAccount(pageable));
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PatchMapping(ApiPath.Notification.MARK_READ)
    public ApiResponse<NotificationRes> markAsRead(
            @PathVariable Long notificationId
    ) {
        return ApiResponse.success(200, MessageConstantsVi.Notification.MARK_AS_READ_SUCCESS,
                notificationService.markAsRead(notificationId));
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PatchMapping(ApiPath.Notification.MARK_READ_ALL)
    public ApiResponse<Void> markAllAsRead() {
        notificationService.markAllAsRead();
        return ApiResponse.success(200, MessageConstantsVi.Notification.MARK_ALL_AS_READ_SUCCESS, null);
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.Notification.BY_ID)
    public ApiResponse<Void> deleteNotification(
            @PathVariable Long notificationId
    ) {
        notificationService.deleteNotification(notificationId);
        return ApiResponse.success(200, MessageConstantsVi.Notification.DELETE_NOTIFICATION_SUCCESS, null);
    }
}