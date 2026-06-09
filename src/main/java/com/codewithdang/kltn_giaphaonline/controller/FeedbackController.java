package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.FeedbackHandleReq;
import com.codewithdang.kltn_giaphaonline.dto.request.FeedbackReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FeedbackRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.service.feedback.FeedbackService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/feedbacks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackController {

    FeedbackService feedbackService;

    @PostMapping
    public ApiResponse<FeedbackRes> createFeedback(@Valid @RequestBody FeedbackReq req) {
        return ApiResponse.success(201, "Gửi feedback thành công", feedbackService.CreateFeedback(req));
    }

    @PatchMapping("/{feedbackId}/handle")
    public ApiResponse<FeedbackRes> handleFeedback(
            @PathVariable Long feedbackId,
            @Valid @RequestBody FeedbackHandleReq req
    ) {
        return ApiResponse.success(200, "Xử lý feedback thành công", feedbackService.handleFeedback(feedbackId, req));
    }

    @GetMapping("/{id}")
    public ApiResponse<FeedbackRes> getFeedbackById(@PathVariable Long id) {
        return ApiResponse.success(200, "Lấy feedback thành công", feedbackService.getFeedbackById(id));
    }

    @GetMapping
    public ApiResponse<PageResponse<FeedbackRes>> getAllByAccountId(
            @RequestParam(required = false) String subject,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponse.success(200, "Lấy danh sách feedback thành công",
                feedbackService.getAllByCurrentAccount(subject, pageable));
    }

    @GetMapping("/all")
    public ApiResponse<PageResponse<FeedbackRes>> getAll(
            @RequestParam(required = false) String keyword,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponse.success(200, "Lấy danh sách feedback thành công",
                feedbackService.getAll(keyword, pageable));
    }

    @DeleteMapping("/{feedbackId}")
    public ApiResponse<Void> deleteFeedback(@PathVariable Long feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
        return ApiResponse.success(200, "Xóa feedback thành công", null);
    }
}
