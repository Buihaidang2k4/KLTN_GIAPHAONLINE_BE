package com.codewithdang.kltn_giaphaonline.service.family_member;

import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyMemberRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyMemberRes;
import com.codewithdang.kltn_giaphaonline.entity.*;
import com.codewithdang.kltn_giaphaonline.enums.*;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.FamilyMemberMapper;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyMemberRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyRepo;
import com.codewithdang.kltn_giaphaonline.repo.RoleRepo;
import com.codewithdang.kltn_giaphaonline.service.notification.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FamilyMemberServiceImpl implements FamilyMemberService {
    FamilyMemberRepo familyMemberRepo;
    FamilyRepo familyRepo;
    AccountRepo accountRepo;
    RoleRepo roleRepo;
    NotificationService notificationService;
    FamilyMemberMapper memberMapper;

    @Override
    @Transactional(readOnly = true)
    public List<FamilyMemberRes> getFamilyMemberByFamilyId(Long familyId) {
        List<FamilyMember> familyMember = familyMemberRepo.findByFamily_FamilyIdAndStatus(familyId, FamilyMemberStatus.ACTIVE);
        return familyMember.stream().map(memberMapper::toRes).toList();
    }

    /***
     * Create family default when register new account
     * @param familyId
     * @param accountId
     */
    @Override
    public void assignFamilyAdmin(Long familyId, Long accountId) {
        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        Role adminRole = roleRepo.findByName(RoleEnums.FAMILY_ADMIN.name())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        saveOrUpdateMembership(family, account, adminRole);
        log.info("Successfully assigned Admin for Family: {} to Account: {}", familyId, accountId);
    }

    /***
     * add member when invitation by family ADMIN
     * @param familyId
     * @param accountId
     * @param roleName
     * @return
     */
    @Override
    @Transactional
    public FamilyMember addMember(Long familyId, Long accountId, String roleName) {
        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        // 1. Kiểm tra trạng thái tài khoản
        if (account.getAccountStatus() != AccountStatus.ACTIVE)
            throw new AppException(ErrorCode.ACCOUNT_STATUS_IS_NOT_ACTIVE);

        // 2. Kiểm tra Role và Scope (Phải là role thuộc Gia phả)
        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        if (role.getScopeType() != RoleScopeType.FAMILY)
            throw new AppException(ErrorCode.ROLE_IS_NOT_WITHIN_THE_SCOPE_OF_THE_GENEALOGY);

        // 3. Kiểm tra xem đã là thành viên ACTIVE chưa
        boolean alreadyIn = familyMemberRepo.existsByFamily_FamilyIdAndAccount_AccountIdAndStatus(
                familyId, accountId, FamilyMemberStatus.ACTIVE);
        if (alreadyIn)
            throw new AppException(ErrorCode.THIS_ACCOUNT_IS_ALREADY_A_MEMBER_OF_THE_FAMILY);

        return saveOrUpdateMembership(family, account, role);
    }


    @Override
    @Transactional
    public void updateMemberRole(Long familyId, Long targetAccountId, UpdateFamilyMemberRoleReq memberRoleReq, Long actorAccountId) {
        // validate actor quyền quản lý member
        validateFamilyAdmin(familyId, actorAccountId);

        // tìm member target
        if (targetAccountId.equals(actorAccountId)) {
            throw new AppException(ErrorCode.CANNOT_UPDATE_YOUR_OWN_ROLE);
        }

        Role newRole = roleRepo.findByName(memberRoleReq.getRoleName()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        validateFamilyRole(newRole);

        FamilyMember targetMember = getActiveMember(familyId, targetAccountId);

        // check role da ton tai chua
        if (targetMember.getRole() != null
                && targetMember.getRole().getName().equals(newRole.getName())) {
            throw new AppException(ErrorCode.ROLE_ALREADY_ASSIGNED_TO_MEMBER);
        }

        // đổi role
        targetMember.setRole(newRole);
        familyMemberRepo.save(targetMember);

        notificationService.createNotification(
                targetAccountId,
                actorAccountId,
                NotificationType.FAMILY_MEMBER_ROLE_CHANGED,
                "Vai trò trong gia phả đã được cập nhật",
                "Vai trò của bạn trong gia phả đã được đổi thành " + newRole.getName(),
                familyId,
                "FAMILY",
                "/families/" + familyId + "/members"
        );
    }

    @Override
    @Transactional
    public void removeMember(Long familyId, Long targetAccountId, Long actorAccountId) {
        // validate actor quyền quản lý member
        validateFamilyAdmin(familyId, actorAccountId);

        if (targetAccountId.equals(actorAccountId)) {
            throw new AppException(ErrorCode.CANNOT_UPDATE_YOUR_OWN_ROLE);
        }

        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));

        FamilyMember targetMember = getActiveMember(familyId, targetAccountId);

        targetMember.setStatus(FamilyMemberStatus.REMOVED);
        targetMember.setRemovedAt(Instant.now());
        familyMemberRepo.save(targetMember);

        notificationService.createNotification(
                targetAccountId,
                actorAccountId,
                NotificationType.FAMILY_MEMBER_REMOVED,
                "Bạn đã bị xóa khỏi gia phả",
                "Bạn đã bị xóa khỏi gia phả " + family.getFamilyName(),
                familyId,
                "FAMILY",
                "/families"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isActiveMember(Long familyId, Long accountId) {
        return familyMemberRepo.existsByFamily_FamilyIdAndAccount_AccountIdAndStatus(
                familyId, accountId, FamilyMemberStatus.ACTIVE
        );
    }

    private FamilyMember saveOrUpdateMembership(Family family, Account account, Role role) {
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

        return familyMemberRepo.save(member);
    }


    private FamilyMember getActiveMember(Long familyId, Long accountId) {
        FamilyMember member = familyMemberRepo.findByFamily_FamilyIdAndAccount_AccountId(familyId, accountId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_MEMBER_NOT_EXISTED));

        if (member.getStatus() != FamilyMemberStatus.ACTIVE) {
            throw new AppException(ErrorCode.FAMILY_MEMBER_STATUS_NOT_ACTIVE);
        }

        return member;
    }

    private void validateFamilyAdmin(Long familyId, Long accountId) {
        FamilyMember actor = getActiveMember(familyId, accountId);

        if (actor.getRole() == null || !RoleEnums.FAMILY_ADMIN.name().equals(actor.getRole().getName())) {
            throw new AppException(ErrorCode.FAMILY_ROLE_IS_NOT_AUTHORITY);
        }
    }

    private void validateFamilyRole(Role role) {
        if (role.getScopeType() != RoleScopeType.FAMILY) {
            throw new AppException(ErrorCode.ROLE_IS_NOT_WITHIN_THE_SCOPE_OF_THE_GENEALOGY);
        }
    }

}
