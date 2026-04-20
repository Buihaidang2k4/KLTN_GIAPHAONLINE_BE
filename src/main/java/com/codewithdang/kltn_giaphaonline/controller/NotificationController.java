package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.NotificationRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.service.notification.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/notifications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    NotificationService notificationService;

    @GetMapping
    public ApiResponse<PageResponse<NotificationRes>> getMyNotifications(
            @RequestParam Long recipientAccountId,
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ApiResponse.success(200, "Lấy danh sách thông báo thành công",
                notificationService.getNotifications(recipientAccountId, pageable));
    }

    @GetMapping("/unread")
    public ApiResponse<PageResponse<NotificationRes>> getUnreadNotifications(
            @RequestParam Long recipientAccountId,
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ApiResponse.success(200, "Lấy danh sách thông báo chưa đọc thành công",
                notificationService.getUnreadNotifications(recipientAccountId, pageable));
    }

    @GetMapping("/unread-count")
    public ApiResponse<Long> countUnreadNotifications(
            @RequestParam Long recipientAccountId
    ) {
        return ApiResponse.success(200, "Truy vấn số lượng thông báo chưa đọc thành công",
                notificationService.countUnreadNotifications(recipientAccountId));
    }

    @PatchMapping("/{notificationId}/read")
    public ApiResponse<NotificationRes> markAsRead(
            @PathVariable Long notificationId,
            @RequestParam Long recipientAccountId
    ) {
        return ApiResponse.success(200, "Đã đánh dấu thông báo là đã đọc",
                notificationService.markAsRead(notificationId, recipientAccountId));
    }

    @PatchMapping("/read-all")
    public ApiResponse<Void> markAllAsRead(
            @RequestParam Long recipientAccountId
    ) {
        notificationService.markAllAsRead(recipientAccountId);
        return ApiResponse.success(200, "Đã đánh dấu tất cả thông báo là đã đọc", null);
    }

    @DeleteMapping("/{notificationId}")
    public ApiResponse<Void> deleteNotification(
            @PathVariable Long notificationId,
            @RequestParam Long recipientAccountId
    ) {
        notificationService.deleteNotification(notificationId, recipientAccountId);
        return ApiResponse.success(200, "Xóa thông báo thành công", null);
    }
}