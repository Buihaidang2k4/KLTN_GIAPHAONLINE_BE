package com.codewithdang.kltn_giaphaonline.service.family_invitation;


import com.codewithdang.kltn_giaphaonline.dto.request.CreateAuditLogReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CreateFamilyInvitationReq;
import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailInvitationAccount;
import com.codewithdang.kltn_giaphaonline.dto.response.InviteInvitationMemberRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.*;
import com.codewithdang.kltn_giaphaonline.enums.*;
import com.codewithdang.kltn_giaphaonline.enums.AuditEntityType;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.FamilyInvitationMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.*;
import com.codewithdang.kltn_giaphaonline.service.account.AccountService;
import com.codewithdang.kltn_giaphaonline.service.audit_log.AuditLogService;
import com.codewithdang.kltn_giaphaonline.service.family_member.FamilyMemberService;
import com.codewithdang.kltn_giaphaonline.service.notification.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FamilyInvitationServiceImpl implements FamilyInvitationService {
    FamilyInvitationRepo invitationRepo;
    FamilyRepo familyRepo;
    FamilyMemberRepo familyMemberRepo;
    RoleRepo roleRepo;
    FamilyInvitationMapper familyInvitationMapper;
    AccountRepo accountRepo;
    ApplicationEventPublisher eventPublisher;
    NotificationService notificationService;
    FamilyMemberService familyMemberService;
    AuditLogService auditLogService;
    PageMapper pageMapper;

    /***
     * list of invitations sent by the current user.
     * @param pageable
     * @return
     */
    @Override
    public PageResponse<InviteInvitationMemberRes> getMyInvitationsSent(Pageable pageable) {
        Account currentAccount = getCurrentAccount();

        Page<FamilyInvitation> invitationsSentPage = invitationRepo.findByInvitedByAccount_AccountId(currentAccount.getAccountId(), pageable);
        return pageMapper.toPageResponse(
                invitationsSentPage,
                familyInvitationMapper::toRes
        );
    }

    /***
     * list of invitations received by the current user's email.
     * @param pageable
     * @return
     */
    @Override
    public PageResponse<InviteInvitationMemberRes> getMyInvitationsReceived(Pageable pageable) {
        Account currentAccount = getCurrentAccount();
        Page<FamilyInvitation> invitationReceivedPage = invitationRepo.findByInvitedEmailIgnoreCase(currentAccount.getEmail(), pageable);

        return pageMapper.toPageResponse(invitationReceivedPage, familyInvitationMapper::toRes);
    }

    /***
     * Create and send a new family invitation (own admin family)
     * @param familyId
     * @param invitationReq
     * @return
     */
    @Override
    @Transactional
    public InviteInvitationMemberRes inviteMember(
            Long familyId, CreateFamilyInvitationReq invitationReq
    ) {
        Account inviterAccount = getCurrentAccount();

        Family family = familyRepo.findById(familyId).orElseThrow(
                () -> new AppException(ErrorCode.FAMILY_NOT_EXISTED)
        );

        // check owner family role account
        validateFamilyAdmin(familyId, inviterAccount.getAccountId());

        // check role
        Role role = roleRepo.findByName(invitationReq.getRoleName())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        validateFamilyRole(role);

        String invitedEmail = invitationReq.getInvitedEmail().trim().toLowerCase();
        String invitationToken = UUID.randomUUID().toString();
        Instant expiryTime = Instant.now().plus(1, ChronoUnit.DAYS);

        // check account
        Account invitedAccount = accountRepo.findByEmail(invitedEmail).orElse(null);
        checkInvitationEligibility(family.getFamilyId(), inviterAccount, invitedEmail, invitedAccount);

        // build entity
        FamilyInvitation familyInvitation = FamilyInvitation.builder()
                .family(family)
                .invitedEmail(invitedEmail)
                .role(role)
                .inviteToken(invitationToken)
                .invitedByAccount(inviterAccount)
                .invitationStatus(FamilyInvitationStatus.PENDING)
                .message(invitationReq.getMessage())
                .expiredAt(expiryTime)
                .build();

        if (invitedAccount != null) familyInvitation.setInvitedAccount(invitedAccount);

        familyInvitation = invitationRepo.save(familyInvitation);

        // 5. Gửi thông báo
        handleNotificationDispatch(family, inviterAccount, invitedAccount, familyInvitation);

        auditLogService.log(CreateAuditLogReq.builder()
                .familyId(family.getFamilyId())
                .actorAccountId(inviterAccount.getAccountId())
                .auditAction(AuditAction.INVITE_MEMBER.getLabel())
                .entityType(AuditEntityType.FAMILY.name())
                .newData(buildInvitationDataMap(familyInvitation))
                .entityId(familyInvitation.getFamilyInvitationId().toString())
                .build());

        return familyInvitationMapper.toRes(familyInvitation);
    }

    /***
     * Accept a family invitation and add the user as an active family member.
     * @param token
     */
    @Override
    @Transactional
    public void acceptInvitation(String token) {
        Account currentAccount = getCurrentAccount();

        FamilyInvitation familyInvitation = invitationRepo.findByInviteToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_INVITATION_NOT_EXISTED));

        // Email token phải khớp với email account đang login
        if (!currentAccount.getEmail().equalsIgnoreCase(familyInvitation.getInvitedEmail()))
            throw new AppException(ErrorCode.INVALID_INVITATION_RECIPIENT);

        if (familyInvitation.getInvitationStatus() != FamilyInvitationStatus.PENDING)
            throw new AppException(ErrorCode.INVITATION_ALREADY_HANDLED);

        if (familyInvitation.getExpiredAt() != null && familyInvitation.getExpiredAt().isBefore(Instant.now()))
            throw new AppException(ErrorCode.INVITATION_EXPIRED);

        Map<String, Object> oldData = buildInvitationDataMap(familyInvitation);

        // check da la thanh vien chua
        boolean isAlreadyMember = familyMemberService.isActiveMember(familyInvitation.getFamily().getFamilyId(), currentAccount.getAccountId());
        if (!isAlreadyMember) {
            familyMemberService.addMember(
                    familyInvitation.getFamily().getFamilyId(),
                    currentAccount.getAccountId(),
                    familyInvitation.getRole().getName()
            );
        }
        updateInvitationStatus(familyInvitation, currentAccount, FamilyInvitationStatus.ACCEPTED);

        auditLogService.log(CreateAuditLogReq.builder()
                .familyId(familyInvitation.getFamily().getFamilyId())
                .actorAccountId(currentAccount.getAccountId())
                .auditAction(AuditAction.ACCEPT_INVITATION.getLabel())
                .entityType(AuditEntityType.FAMILY.name())
                .oldData(oldData)
                .newData(buildInvitationDataMap(familyInvitation))
                .entityId(familyInvitation.getFamilyInvitationId().toString())
                .build());
    }

    @Override
    @Transactional
    public void acceptInvitationForNewAccount(String token, Account account) {
        FamilyInvitation familyInvitation = invitationRepo.findByInviteToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_INVITATION_NOT_EXISTED));

        if (!account.getEmail().equalsIgnoreCase(familyInvitation.getInvitedEmail()))
            throw new AppException(ErrorCode.INVALID_INVITATION_RECIPIENT);

        if (familyInvitation.getInvitationStatus() != FamilyInvitationStatus.PENDING)
            throw new AppException(ErrorCode.INVITATION_ALREADY_HANDLED);

        if (familyInvitation.getExpiredAt() != null && familyInvitation.getExpiredAt().isBefore(Instant.now()))
            throw new AppException(ErrorCode.INVITATION_EXPIRED);

        Map<String, Object> oldData = buildInvitationDataMap(familyInvitation);

        Family family = familyInvitation.getFamily();
        Role role = familyInvitation.getRole();

        FamilyMember member = familyMemberRepo
                .findByFamily_FamilyIdAndAccount_AccountId(family.getFamilyId(), account.getAccountId())
                .orElse(FamilyMember.builder()
                        .id(new FamilyMemberId(family.getFamilyId(), account.getAccountId()))
                        .family(family)
                        .account(account)
                        .build());

        member.setRole(role);
        member.setStatus(FamilyMemberStatus.ACTIVE);
        member.setJoinedAt(Instant.now());
        member.setRemovedAt(null);

        familyMemberRepo.save(member);

        updateInvitationStatus(familyInvitation, account, FamilyInvitationStatus.ACCEPTED);

        auditLogService.log(CreateAuditLogReq.builder()
                .familyId(familyInvitation.getFamily().getFamilyId())
                .actorAccountId(account.getAccountId())
                .auditAction(AuditAction.ACCEPT_INVITATION.getLabel())
                .entityType(AuditEntityType.FAMILY.name())
                .oldData(oldData)
                .newData(buildInvitationDataMap(familyInvitation))
                .entityId(familyInvitation.getFamilyInvitationId().toString())
                .build());
    }

    /***
     * Reject a received family invitation
     * @param inviteToken
     */
    @Override
    @Transactional
    public void rejectInvitation(String inviteToken) {
        Account currentAccount = getCurrentAccount();

        FamilyInvitation invitation = invitationRepo.findByInviteToken(inviteToken)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_INVITATION_NOT_EXISTED));

        if (!currentAccount.getEmail().equalsIgnoreCase(invitation.getInvitedEmail()))
            throw new AppException(ErrorCode.INVALID_INVITATION_RECIPIENT);


        if (invitation.getInvitationStatus() != FamilyInvitationStatus.PENDING)
            throw new AppException(ErrorCode.INVITATION_ALREADY_HANDLED);

        if (invitation.getExpiredAt() != null && invitation.getExpiredAt().isBefore(Instant.now()))
            throw new AppException(ErrorCode.INVITATION_EXPIRED);

        Map<String, Object> oldData = buildInvitationDataMap(invitation);

        updateInvitationStatus(invitation, currentAccount, FamilyInvitationStatus.DECLINED);

        auditLogService.log(CreateAuditLogReq.builder()
                .familyId(invitation.getFamily().getFamilyId())
                .actorAccountId(currentAccount.getAccountId())
                .auditAction(AuditAction.REJECT_INVITATION.getLabel())
                .entityType(AuditEntityType.FAMILY.name())
                .oldData(oldData)
                .newData(buildInvitationDataMap(invitation))
                .entityId(invitation.getFamilyInvitationId().toString())
                .build());
    }

    /***
     * Cancel/Revoke a pending invitation previously sent by the admin.
     * @param invitationId
     */
    @Override
    @Transactional
    public void cancelInvitation(Long invitationId) {
        Account currentAccount = getCurrentAccount();

        FamilyInvitation invitation = invitationRepo.findById(invitationId).orElseThrow(() -> new AppException(ErrorCode.FAMILY_INVITATION_NOT_EXISTED));

        // check own sent
        if (!invitation.getInvitedByAccount().getAccountId().equals(currentAccount.getAccountId()))
            throw new AppException(ErrorCode.ACCOUNT_FAMILY_NO_PERMISSION);

        // check status
        if (invitation.getInvitationStatus() != FamilyInvitationStatus.PENDING)
            throw new AppException(ErrorCode.INVITATION_ALREADY_HANDLED);

        Map<String, Object> oldData = buildInvitationDataMap(invitation);

        // update status
        invitation.setInvitationStatus(FamilyInvitationStatus.CANCELED);
        invitation.setHandledAt(Instant.now());
        invitationRepo.save(invitation);

        auditLogService.log(CreateAuditLogReq.builder()
                .familyId(invitation.getFamily().getFamilyId())
                .actorAccountId(currentAccount.getAccountId())
                .auditAction(AuditAction.CANCEL_INVITATION.getLabel())
                .entityType(AuditEntityType.FAMILY.name())
                .oldData(oldData)
                .newData(buildInvitationDataMap(invitation))
                .entityId(invitation.getFamilyInvitationId().toString())
                .build());
    }

    private void handleNotificationDispatch(Family family, Account inviter, Account invited, FamilyInvitation invitation) {
        if (invited != null) {
            notificationService.createNotification(
                    invited.getAccountId(),
                    inviter.getAccountId(),
                    NotificationType.FAMILY_INVITATION,
                    "Lời mời gia nhập dòng họ",
                    String.format("Bạn được mời tham gia %s bởi %s", family.getFamilyName(), inviter.getFullName()),
                    invitation.getFamilyInvitationId(),
                    "FAMILY_INVITATION",
                    "/family-invitations/" + invitation.getFamilyInvitationId()
            );
        } else {
            eventPublisher.publishEvent(
                    EmailInvitationAccount.builder()
                            .toEmail(invitation.getInvitedEmail())
                            .subject("Bạn được mời tham gia dòng họ " + family.getFamilyName())
                            .senderFullName(inviter.getFullName())
                            .familyName(family.getFamilyName())
                            .invitationToken(invitation.getInviteToken())
                            .expiryHours(invitation.getExpiredAt())
                            .personalMessage(invitation.getMessage())
                            .build()
            );

        }
    }

    private void updateInvitationStatus(FamilyInvitation invitation, Account account, FamilyInvitationStatus status) {
        invitation.setInvitedAccount(account);
        invitation.setInvitationStatus(status);
        invitation.setHandledAt(Instant.now());
        invitationRepo.save(invitation);
    }

    private void validateFamilyAdmin(Long familyId, Long accountId) {
        FamilyMember actor = familyMemberRepo
                .findByFamily_FamilyIdAndAccount_AccountId(familyId, accountId)
                .orElseThrow(() -> new AppException(ErrorCode.YOU_ARE_NOT_A_MEMBER_OF_THE_FAMILY));

        if (actor.getStatus() != FamilyMemberStatus.ACTIVE)
            throw new AppException(ErrorCode.FAMILY_MEMBER_STATUS_NOT_ACTIVE);

        if (actor.getRole() == null
                || !RoleEnums.FAMILY_ADMIN.name().equals(actor.getRole().getName())) {
            throw new AppException(ErrorCode.FAMILY_ROLE_IS_NOT_AUTHORITY);
        }
    }

    private void validateFamilyRole(Role role) {
        if (role.getScopeType() != RoleScopeType.FAMILY)
            throw new AppException(ErrorCode.ROLE_IS_NOT_WITHIN_THE_SCOPE_OF_THE_GENEALOGY);
    }

    private void checkInvitationEligibility(Long familyId, Account inviter, String invitedEmail, Account invitedAccount) {
        // 1. Không tự mời chính mình
        if (inviter.getEmail().equalsIgnoreCase(invitedEmail))
            throw new AppException(ErrorCode.CANNOT_INVITE_YOURSELF);

        // 2. Check xem đã có lời mời nào đang chờ không
        if (invitationRepo.existsByFamily_FamilyIdAndInvitedEmailAndInvitationStatus(
                familyId, invitedEmail, FamilyInvitationStatus.PENDING))
            throw new AppException(ErrorCode.THIS_MEMBER_HAS_ALREADY_RECEIVED_AN_INVITATION);

        // 3. Nếu account đã tồn tại, check xem đã là thành viên chưa
        if (invitedAccount != null) {
            if (familyMemberRepo.existsByFamily_FamilyIdAndAccount_AccountIdAndStatus(
                    familyId, invitedAccount.getAccountId(), FamilyMemberStatus.ACTIVE))
                throw new AppException(ErrorCode.THIS_ACCOUNT_IS_ALREADY_A_MEMBER_OF_THE_FAMILY);

            if (invitedAccount.getAccountStatus() != AccountStatus.ACTIVE)
                throw new AppException(ErrorCode.ACCOUNT_STATUS_IS_NOT_ACTIVE);
        }
    }

    /***
     * build data audit
     * @param invitation
     * @return
     */
    private Map<String, Object> buildInvitationDataMap(FamilyInvitation invitation) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("Dòng họ", invitation.getFamily().getFamilyName());
        data.put("Email được mời", invitation.getInvitedEmail());
        data.put("Vai trò", invitation.getRole().getName());
        data.put("Trạng thái", invitation.getInvitationStatus().name());
        data.put("Người mời", invitation.getInvitedByAccount().getFullName());
        if (invitation.getMessage() != null) data.put("Lời nhắn", invitation.getMessage());
        if (invitation.getExpiredAt() != null) data.put("Hết hạn", invitation.getExpiredAt());
        if (invitation.getHandledAt() != null) data.put("Xử lý lúc", invitation.getHandledAt());
        return data;
    }

    //
    private Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return accountRepo.findByEmail(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
    }
}
