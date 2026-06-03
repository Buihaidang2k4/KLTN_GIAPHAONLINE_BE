package com.codewithdang.kltn_giaphaonline.dto.response;

import com.codewithdang.kltn_giaphaonline.enums.FeedbackStatus;
import com.codewithdang.kltn_giaphaonline.enums.FeedbackType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedbackRes {
    Long feedbackId;
    String email;
    FeedbackType type;
    String subject;
    String content;
    FeedbackStatus status;
    String adminResponse;
    LocalDateTime createdAt;
    LocalDateTime resolvedAt;
}
