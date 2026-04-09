package com.codewithdang.kltn_giaphaonline.service.family_invitation;


import com.codewithdang.kltn_giaphaonline.dto.request.CreateFamilyInvitationReq;
import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailInvitationAccount;
import com.codewithdang.kltn_giaphaonline.dto.response.InviteMemberRes;
import com.codewithdang.kltn_giaphaonline.entity.*;
import com.codewithdang.kltn_giaphaonline.enums.*;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.FamilyInvitationMapper;
import com.codewithdang.kltn_giaphaonline.repo.*;
import com.codewithdang.kltn_giaphaonline.service.family_member.FamilyMemberService;
import com.codewithdang.kltn_giaphaonline.service.notification.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
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

    @Override
    @Transactional
    public InviteMemberRes inviteMember(
            Long familyId, Long inviterAccountId, CreateFamilyInvitationReq invitationReq
    ) {
        Family family = familyRepo.findById(familyId).orElseThrow(
                () -> new AppException(ErrorCode.FAMILY_NOT_EXISTED)
        );

        Account inviter = accountRepo.findById(inviterAccountId).orElseThrow(
                () -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED)
        );

        // check owner family role account
        validateFamilyAdmin(familyId, inviterAccountId);

        //check role
        Role role = roleRepo.findByName(invitationReq.getRoleName())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        validateFamilyRole(role);

        String invitedEmail = invitationReq.getInvitedEmail().trim().toLowerCase();
        String invitationToken = UUID.randomUUID().toString();
        Instant expiryTime = Instant.now().plus(1, ChronoUnit.DAYS);

        // build entity
        FamilyInvitation familyInvitation = FamilyInvitation.builder()
                .family(family)
                .invitedEmail(invitedEmail)
                .role(role)
                .inviteToken(invitationToken)
                .invitedByAccount(inviter)
                .invitationStatus(FamilyInvitationStatus.PENDING)
                .message(invitationReq.getMessage())
                .expiredAt(expiryTime)
                .build();

        // check account
        Account invitedAccount = accountRepo.findByEmail(invitedEmail).orElse(null);
        checkAccountValid(family.getFamilyId(), inviter, invitedEmail, invitedAccount);
        if (invitedAccount != null) familyInvitation.setInvitedAccount(invitedAccount);

        familyInvitation = invitationRepo.save(familyInvitation);

        // send action
        if (invitedAccount != null) {
            // push notification In-app
            notificationService.createNotification(
                    invitedAccount.getAccountId(),
                    inviter.getAccountId(),
                    NotificationType.FAMILY_INVITATION,
                    "Lời mời tham gia gia phả",
                    "Bạn đã được mời tham gia gia phả " + family.getFamilyName()
                            + " với vai trò " + role.getName(),
                    familyInvitation.getFamilyInvitationId(),
                    "FAMILY_INVITATION",
                    "/family-invitations/" + familyInvitation.getFamilyInvitationId()
            );

        } else {
            // send link register account email
            eventPublisher.publishEvent(
                    new EmailInvitationAccount(
                            inviter.getFullName(),
                            family.getFamilyName(),
                            invitationToken,
                            expiryTime,
                            invitationReq.getMessage()
                    )
            );
        }

        return familyInvitationMapper.toRes(familyInvitation);
    }

    @Override
    public void acceptInvitation(String token, Long accountId) {
        FamilyInvitation familyInvitation = invitationRepo.findByInviteToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_INVITATION_NOT_EXISTED));

        if (familyInvitation.getInvitationStatus() != FamilyInvitationStatus.PENDING)
            throw new AppException(ErrorCode.INVITATION_ALREADY_HANDLED);

        if (familyInvitation.getExpiredAt() != null && familyInvitation.getExpiredAt().isBefore(Instant.now()))
            throw new AppException(ErrorCode.INVITATION_EXPIRED);

        // get account dang thuc hien
        Account account = accountRepo.findById(accountId).orElseThrow(
                () -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED)
        );

        if (!account.getEmail().equalsIgnoreCase(familyInvitation.getInvitedEmail()))
            throw new AppException(ErrorCode.INVALID_INVITATION_RECIPIENT);

        // check duplicate account family
        boolean alreadyMember = familyMemberRepo.existsByFamily_FamilyIdAndAccount_AccountIdAndStatus(
                familyInvitation.getFamily().getFamilyId(),
                accountId,
                FamilyMemberStatus.ACTIVE
        );

        if (alreadyMember) {
            // set success
            completeInvitation(familyInvitation, account);
            return;
        }

        if (!familyMemberService.isActiveMember(familyInvitation.getFamily().getFamilyId(), accountId)) {
            familyMemberService.addMember(
                    familyInvitation.getFamily().getFamilyId(),
                    accountId,
                    familyInvitation.getRole().getName()
            );
        }
        completeInvitation(familyInvitation, account);
    }

    @Override
    public void rejectInvitation(String inviteToken, Long currentAccountId) {
        FamilyInvitation invitation = invitationRepo.findByInviteToken(inviteToken)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_INVITATION_NOT_EXISTED));

        if (invitation.getInvitationStatus() != FamilyInvitationStatus.PENDING) {
            throw new AppException(ErrorCode.INVITATION_ALREADY_HANDLED);
        }

        if (invitation.getExpiredAt() != null && invitation.getExpiredAt().isBefore(Instant.now())) {
            throw new AppException(ErrorCode.INVITATION_EXPIRED);
        }

        Account currentAccount = accountRepo.findById(currentAccountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        if (!currentAccount.getEmail().equalsIgnoreCase(invitation.getInvitedEmail())) {
            throw new AppException(ErrorCode.INVALID_INVITATION_RECIPIENT);
        }

        invitation.setInvitedAccount(currentAccount);
        invitation.setInvitationStatus(FamilyInvitationStatus.DECLINED);
        invitation.setHandledAt(Instant.now());
        invitationRepo.save(invitation);

    }

    private void completeInvitation(FamilyInvitation invitation, Account account) {
        invitation.setInvitedAccount(account);
        invitation.setInvitationStatus(FamilyInvitationStatus.ACCEPTED);
        invitation.setHandledAt(Instant.now());
        invitationRepo.save(invitation);
    }

    private void validateFamilyAdmin(Long familyId, Long accountId) {
        FamilyMember actor = familyMemberRepo.findByFamily_FamilyIdAndAccount_AccountId(familyId, accountId).
                orElseThrow(() -> new AppException(ErrorCode.YOU_ARE_NOT_A_MEMBER_OF_THE_FAMILY));

        if (actor.getStatus() != FamilyMemberStatus.ACTIVE)
            throw new AppException(ErrorCode.FAMILY_MEMBER_STATUS_NOT_ACTIVE);

        if (actor.getRole() == null || !RoleEnums.FAMILY_ADMIN.name().equals(actor.getRole().getName()))
            throw new AppException(ErrorCode.FAMILY_ROLE_IS_NOT_AUTHORITY);

    }

    private void validateFamilyRole(Role role) {
        if (role.getScopeType() != RoleScopeType.FAMILY)
            throw new AppException(ErrorCode.ROLE_IS_NOT_WITHIN_THE_SCOPE_OF_THE_GENEALOGY);
    }

    private void checkAccountValid(Long familyId, Account inviter, String invitedEmail, Account invitedAccount) {
        boolean hasPendingInvitation =
                invitationRepo.existsByFamily_FamilyIdAndInvitedEmailAndInvitationStatus(
                        familyId, invitedEmail, FamilyInvitationStatus.PENDING
                );

        if (hasPendingInvitation)
            throw new AppException(ErrorCode.THIS_MEMBER_HAS_ALREADY_RECEIVED_AN_INVITATION);

        // check account if existed Then you won't be an active member.
        if (invitedAccount != null) {
            boolean alreadyMember = familyMemberRepo.existsByFamily_FamilyIdAndAccount_AccountIdAndStatus(
                    familyId,
                    invitedAccount.getAccountId(),
                    FamilyMemberStatus.ACTIVE
            );

            if (alreadyMember) throw new AppException(ErrorCode.THIS_ACCOUNT_IS_ALREADY_A_MEMBER_OF_THE_FAMILY);
            // check account status
            if (invitedAccount.getAccountStatus() != AccountStatus.ACTIVE)
                throw new AppException(ErrorCode.ACCOUNT_STATUS_IS_NOT_ACTIVE);
        }

        // check invite yourself
        if (inviter.getEmail().equalsIgnoreCase(invitedEmail))
            throw new AppException(ErrorCode.CANNOT_INVITE_YOURSELF);


    }
}
