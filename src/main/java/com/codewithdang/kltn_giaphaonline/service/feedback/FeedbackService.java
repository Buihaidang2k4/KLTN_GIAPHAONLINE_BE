package com.codewithdang.kltn_giaphaonline.service.feedback;

import com.codewithdang.kltn_giaphaonline.dto.request.FeedbackHandleReq;
import com.codewithdang.kltn_giaphaonline.dto.request.FeedbackReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FeedbackRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface FeedbackService {
    FeedbackRes CreateFeedback(FeedbackReq req);

    FeedbackRes handleFeedback(Long feedbackId, FeedbackHandleReq req);

    FeedbackRes getFeedbackById(Long id);

    PageResponse<FeedbackRes> getAllByCurrentAccount(String subject, Pageable pageable);

    PageResponse<FeedbackRes> getAll(String keyword, Pageable pageable);
}
