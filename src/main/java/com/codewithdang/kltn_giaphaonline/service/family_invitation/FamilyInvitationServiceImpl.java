package com.codewithdang.kltn_giaphaonline.service.family_invitation;


import com.codewithdang.kltn_giaphaonline.dto.event.SendInvitationEmailEvent;
import com.codewithdang.kltn_giaphaonline.dto.request.CreateFamilyInvitationReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyMemberRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.InviteMemberRes;
import com.codewithdang.kltn_giaphaonline.entity.*;
import com.codewithdang.kltn_giaphaonline.enums.FamilyInvitationStatus;
import com.codewithdang.kltn_giaphaonline.enums.FamilyMemberStatus;
import com.codewithdang.kltn_giaphaonline.enums.RoleEnums;
import com.codewithdang.kltn_giaphaonline.enums.RoleScopeType;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.FamilyInvitationMapper;
import com.codewithdang.kltn_giaphaonline.repo.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

        // check xem có lời mời chưa
        boolean hasPendingInvitation =
                invitationRepo.existsByFamily_FamilyIdAndInvitedEmailAndInvitationStatus(
                        familyId, invitedEmail, FamilyInvitationStatus.PENDING
                );

        if (hasPendingInvitation)
            throw new AppException(ErrorCode.THIS_MEMBER_HAS_ALREADY_RECEIVED_AN_INVITATION);

        // check account if existed Then you won't be an active member.
        Account invitedAccount = accountRepo.findByEmail(invitedEmail).orElse(null);
        if (invitedAccount != null) {
            boolean alreadyMember = familyMemberRepo.existsByFamily_FamilyIdAndAccount_AccountIdAndStatus(
                    familyId,
                    invitedAccount.getAccountId(),
                    FamilyMemberStatus.ACTIVE
            );

            if (alreadyMember) throw new AppException(ErrorCode.THIS_ACCOUNT_IS_ALREADY_A_MEMBER_OF_THE_FAMILY);
        }

        // build entity
        FamilyInvitation familyInvitation = FamilyInvitation.builder()
                .family(family)
                .invitedEmail(invitedEmail)
                .role(role)
                .inviteToken(UUID.randomUUID().toString())
                .invitedByAccount(inviter)
                .invitationStatus(FamilyInvitationStatus.PENDING)
                .message(invitationReq.getMessage())
                .expiredAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .build();

        // check account
        accountRepo.findByEmail(invitationReq.getInvitedEmail()).ifPresent(familyInvitation::setInvitedAccount);

        // send notification
        if (invitedAccount != null) {
            // send In-app
            
        } else {
            // send link register account
            eventPublisher.publishEvent(
                    new SendInvitationEmailEvent(
                            invitedEmail,
                            familyInvitation.getInviteToken()
                    )
            );
        }

        familyInvitation = invitationRepo.save(familyInvitation);
        return familyInvitationMapper.toRes(familyInvitation);
    }

    @Override
    public void acceptInvitation(String token) {

    }

    @Override
    public void rejectInvitation(String inviteToken, Long currentAccountId) {

    }

    @Override
    public void updateMemberRole(Long familyId, Long targetAccountId, UpdateFamilyMemberRoleReq request, Long actorAccountId) {

    }

    @Override
    public void removeMember(Long familyId, Long targetAccountId, Long actorAccountId) {

    }

    private void validateFamilyAdmin(Long familyId, Long accountId) {
        FamilyMember actor = familyMemberRepo.findByFamily_FamilyIdAndAccount_AccountId(familyId, accountId).
                orElseThrow(() -> new AppException(ErrorCode.YOU_ARE_NOT_A_MEMBER_OF_THE_FAMILY));

        if (actor.getStatus() != FamilyMemberStatus.ACTIVE)
            throw new AppException(ErrorCode.FAMILY_MEMBER_STATUS_NOT_ACTIVE);

        if (actor.getRole() == null || !RoleEnums.FAMILY_ADMIN.equals(actor.getRole()))
            throw new AppException(ErrorCode.FAMILY_ROLE_IS_NOT_AUTHORITY);

    }

    private void validateFamilyRole(Role role) {
        if (role.getScopeType() != RoleScopeType.FAMILY)
            throw new AppException(ErrorCode.ROLE_IS_NOT_WITHIN_THE_SCOPE_OF_THE_GENEALOGY);
    }
}
