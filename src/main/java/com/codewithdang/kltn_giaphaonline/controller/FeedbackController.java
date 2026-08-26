package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.FeedbackHandleReq;
import com.codewithdang.kltn_giaphaonline.dto.request.FeedbackReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FeedbackRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
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
@RequestMapping(ApiPath.Feedback.BASE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackController {

    FeedbackService feedbackService;

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping
    public ApiResponse<FeedbackRes> createFeedback(@Valid @RequestBody FeedbackReq req) {
        return ApiResponse.success(201, MessageConstantsVi.Feedback.CREATE_FEEDBACK_SUCCESS, feedbackService.CreateFeedback(req));
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PatchMapping(ApiPath.Feedback.HANDLE)
    public ApiResponse<FeedbackRes> handleFeedback(
            @PathVariable Long feedbackId,
            @Valid @RequestBody FeedbackHandleReq req
    ) {
        return ApiResponse.success(200, MessageConstantsVi.Feedback.HANDLE_FEEDBACK_SUCCESS, feedbackService.handleFeedback(feedbackId, req));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Feedback.BY_ID)
    public ApiResponse<FeedbackRes> getFeedbackById(@PathVariable Long id) {
        return ApiResponse.success(200, MessageConstantsVi.Feedback.GET_FEEDBACK_SUCCESS, feedbackService.getFeedbackById(id));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping
    public ApiResponse<PageResponse<FeedbackRes>> getAllByAccountId(
            @RequestParam(required = false) String subject,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponse.success(200, MessageConstantsVi.Feedback.GET_FEEDBACK_LIST_SUCCESS,
                feedbackService.getAllByCurrentAccount(subject, pageable));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Feedback.ALL)
    public ApiResponse<PageResponse<FeedbackRes>> getAll(
            @RequestParam(required = false) String keyword,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponse.success(200, MessageConstantsVi.Feedback.GET_FEEDBACK_LIST_SUCCESS,
                feedbackService.getAll(keyword, pageable));
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.Feedback.BY_FEEDBACK_ID)
    public ApiResponse<Void> deleteFeedback(@PathVariable Long feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
        return ApiResponse.success(200, MessageConstantsVi.Feedback.DELETE_FEEDBACK_SUCCESS, null);
    }
}
