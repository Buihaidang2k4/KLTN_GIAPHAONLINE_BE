package com.codewithdang.kltn_giaphaonline.service.feedback;


import com.codewithdang.kltn_giaphaonline.dto.request.FeedbackHandleReq;
import com.codewithdang.kltn_giaphaonline.dto.request.FeedbackReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FeedbackRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.AccountRole;
import com.codewithdang.kltn_giaphaonline.entity.Feedback;
import com.codewithdang.kltn_giaphaonline.enums.AccountStatus;
import com.codewithdang.kltn_giaphaonline.enums.FeedbackStatus;
import com.codewithdang.kltn_giaphaonline.enums.NotificationType;
import com.codewithdang.kltn_giaphaonline.enums.RoleEnums;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.FeedbackMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.AccountRoleRepo;
import com.codewithdang.kltn_giaphaonline.repo.FeedbackRepo;
import com.codewithdang.kltn_giaphaonline.service.notification.NotificationService;
import com.codewithdang.kltn_giaphaonline.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackServiceImpl implements FeedbackService {
    FeedbackRepo feedbackRepo;
    FeedbackMapper feedbackMapper;
    PageMapper pageMapper;
    SecurityUtils securityUtils;
    NotificationService notificationService;
    AccountRoleRepo accountRoleRepo;

    @Override
    @Transactional
    public FeedbackRes CreateFeedback(FeedbackReq req) {
        Account account = securityUtils.getCurrentAccount();
        Feedback feedback = feedbackMapper.toEntity(req);
        feedback.setStatus(FeedbackStatus.PENDING);
        feedback.setAccount(account);
        feedback = feedbackRepo.save(feedback);

        // notification admin
        notifyAdminsAboutNewFeedback(account, feedback);

        return feedbackMapper.toRes(feedback);
    }

    @Override
    @Transactional
    public FeedbackRes handleFeedback(Long feedbackId, FeedbackHandleReq req) {
        Account account = securityUtils.getCurrentAccount();

        Feedback feedback = feedbackRepo.findById(feedbackId).orElseThrow(() ->
                new AppException(ErrorCode.FEEDBACK_NOT_FOUND));
        feedbackMapper.updateEntityFromRequest(req, feedback);
        feedback.setResolvedAt(LocalDateTime.now());
        feedback = feedbackRepo.save(feedback);

        notificationService.createNotification(
                feedback.getAccount().getAccountId(),
                account.getAccountId(),
                NotificationType.FEEDBACK_USER,
                "Phản hồi của bạn đã được xử lý",
                "Feedback \"" + feedback.getSubject() + "\" đã được phản hồi: " + req.getAdminResponse(),
                feedback.getFeedbackId(),
                "FEEDBACK",
                "/feedbacks/" + feedback.getFeedbackId()
        );

        return feedbackMapper.toRes(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackRes getFeedbackById(Long id) {
        Feedback feedback = feedbackRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_FOUND));
        return feedbackMapper.toRes(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FeedbackRes> getAllByCurrentAccount(String subject, Pageable pageable) {
        Account account = securityUtils.getCurrentAccount();
        Page<Feedback> feedbacks = (subject != null && !subject.isBlank())
                ? feedbackRepo.findAllByAccount_AccountIdAndSubjectContainingIgnoreCase(account.getAccountId(), subject, pageable)
                : feedbackRepo.findAllByAccount_AccountId(account.getAccountId(), pageable);
        return pageMapper.toPageResponse(feedbacks, feedbackMapper::toRes);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FeedbackRes> getAll(String keyword, Pageable pageable) {
        String k = (keyword != null && !keyword.isBlank()) ? keyword : null;
        Page<Feedback> feedbacks = (k != null)
                ? feedbackRepo.searchByKeyword(k, pageable)
                : feedbackRepo.findAll(pageable);
        return pageMapper.toPageResponse(feedbacks, feedbackMapper::toRes);
    }

    @Override
    @Transactional
    public void deleteFeedback(Long feedbackId) {
        if (feedbackRepo.existsById(feedbackId))
            feedbackRepo.deleteById(feedbackId);
    }

    private void notifyAdminsAboutNewFeedback(Account sender, Feedback feedback) {
        List<Account> accountsAdmin = accountRoleRepo.findAllByRole_Name(RoleEnums.SYSTEM_ADMIN.name()).stream()
                .map(AccountRole::getAccount)
                .filter(account -> account.getAccountStatus() == AccountStatus.ACTIVE)
                .collect(Collectors.toList());

        for (Account admin : accountsAdmin) {
            notificationService.createNotification(
                    admin.getAccountId(),
                    sender.getAccountId(),
                    NotificationType.FEEDBACK_ADMIN,
                    "Phản hồi mới",
                    "Người dùng " + sender.getFullName() + " đã gửi phản hồi: " + feedback.getSubject(),
                    feedback.getFeedbackId(),
                    "FEEDBACK",
                    "/admin/feedbacks/" + feedback.getFeedbackId()
            );
        }
    }
}
