package com.codewithdang.kltn_giaphaonline.service.account;

import com.codewithdang.kltn_giaphaonline.dto.request.ChangePasswordAccountReq;
import com.codewithdang.kltn_giaphaonline.dto.request.ChangeStatusLockReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CreateAccountReq;
import com.codewithdang.kltn_giaphaonline.dto.request.FamilyReq;
import com.codewithdang.kltn_giaphaonline.dto.response.AccountDetailsRes;
import com.codewithdang.kltn_giaphaonline.dto.response.AccountRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.AccountRole;
import com.codewithdang.kltn_giaphaonline.entity.AccountRoleId;
import com.codewithdang.kltn_giaphaonline.entity.Role;
import com.codewithdang.kltn_giaphaonline.enums.AccountStatus;
import com.codewithdang.kltn_giaphaonline.enums.RoleScopeType;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.AccountMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.*;
import com.codewithdang.kltn_giaphaonline.service.family.FamilyService;
import com.codewithdang.kltn_giaphaonline.service.minio.MinioService;
import com.codewithdang.kltn_giaphaonline.service.role.RoleService;
import com.codewithdang.kltn_giaphaonline.utils.ConstantUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl implements AccountService {
    AccountRepo accountRepo;
    RoleRepo roleRepo;
    AccountMapper accountMapper;
    PasswordEncoder passwordEncoder;
    MinioService minioService;
    AccountRoleRepo accountRoleRepo;
    PageMapper pageMapper;
    FamilyService familyService;
    RoleService roleService;
    AccountVerificationTokenRepo verificationTokenRepo;
    AuditLogRepo auditLogRepo;
    FamilyRepo familyRepo;
    FamilyInvitationRepo familyInvitationRepo;
    FamilyMemberRepo familyMemberRepo;
    NotificationRepo notificationRepo;

    @Override
    @Transactional(readOnly = true)
    public Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return accountRepo.findByEmail(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public AccountRes createAccount(CreateAccountReq req) {
        if (accountRepo.existsByEmail(req.email()))
            throw new AppException(ErrorCode.ACCOUNT_EXISTED);

        if (!req.isPasswordMatching())
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);

        Account account = accountMapper.toEntity(req);
        account.setPasswordHash(passwordEncoder.encode(req.password()));
        account.setAccountStatus(AccountStatus.ACTIVE);
        accountRepo.save(account);

        roleService.assignRoleToAccount(account, req.roleEnums());

        if (req.roleEnums().getScopeType() == RoleScopeType.FAMILY) {
            // tao dong ho
            familyService.createFamily(FamilyReq.builder()
                    .ownerAccountId(account.getAccountId())
                    .familyName(req.familyName())
                    .build());
        }

        return accountMapper.toRes(account);
    }

    @Override
    @Transactional
    public void changePassword(Long accountId, ChangePasswordAccountReq req) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        // check pass old
        if (!passwordEncoder.matches(req.oldPassword(), account.getPasswordHash()))
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);

        if (!req.newPassword().matches(req.confirmPassword()))
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);

        account.setPasswordHash(passwordEncoder.encode(req.newPassword()));
        accountRepo.save(account);
        log.info("=== Change Password Successfully  Account Id = {} ===", accountId);
    }

    // lock , unlock
    @Override
    @Transactional
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public AccountRes updateAccountStatus(Long accountId, ChangeStatusLockReq req) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        if (account.getAccountStatus().equals(AccountStatus.ACTIVE)
                && req.accountStatus().equals(AccountStatus.LOCKED)
        ) {
            account.setAccountStatus(req.accountStatus());
            account.setLockReason(req.lockReason());
            account.setLockedAt(LocalDateTime.now());

        } else if (account.getAccountStatus().equals(AccountStatus.LOCKED)
                && req.accountStatus().equals(AccountStatus.ACTIVE)
        ) {
            account.setAccountStatus(req.accountStatus());
            account.setLockReason(null);
            account.setLockedAt(null);
        } else {
            throw new AppException(ErrorCode.ACCOUNT_CANNOT_UPDATE_STATUS);
        }
        return accountMapper.toRes(account);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public void softDeleteAccount(Long accountId) {
        Account account = accountRepo.findById(accountId).
                orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        account.setAccountStatus(AccountStatus.DELETED);
        accountRepo.save(account);
    }

    /***
     * Only delete accounts that have a delete status
     * @param accountId
     */
    @Override
    @Transactional
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public void hardDeleteAccount(Long accountId) {
        Account account = accountRepo.findById(accountId).
                orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        if (account.getAccountStatus() != AccountStatus.DELETED)
            throw new AppException(ErrorCode.ACCOUNT_STATUS_IS_NOT_DELETE);

        // delete verify token
        verificationTokenRepo.deleteByAccount(account);
        // delete audit log
        auditLogRepo.deleteByActor(account);
        // delete family_invitations
        familyInvitationRepo.deleteByInvitedAccount(account);
        familyInvitationRepo.deleteByInvitedByAccount(account);
        // delete family member
        familyMemberRepo.deleteByAccount(account);
        // delete notification
        notificationRepo.deleteByRecipient(account);
        notificationRepo.deleteBySender(account);
        // delete family
        familyRepo.deleteByOwner(account);
        // delete account
        accountRoleRepo.deleteByAccount(account);
        accountRepo.delete(account);

        log.info("=== Hard Deleted Account Successfully | ID: {} ===", accountId);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public void addRole(Long accountId, String roleName) {
        Account account = accountRepo.findById(accountId).orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
        Role role = roleRepo.findById(roleName).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        if (accountRoleRepo.existsByRoleNameAndAccount_AccountId(roleName, accountId))
            throw new AppException(ErrorCode.ROLE_IS_ALREADY_IN_ACCOUNT);

        AccountRoleId accountRoleId = AccountRoleId.builder()
                .accountId(accountId)
                .roleName(roleName)
                .build();

        AccountRole accountRole = AccountRole.builder()
                .id(accountRoleId)
                .account(account)
                .role(role)
                .build();

        accountRoleRepo.save(accountRole);
        if (account.getAccountRoles() == null) {
            account.setAccountRoles(new HashSet<>());
        }
        account.getAccountRoles().add(accountRole);
        accountRepo.save(account);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public void removeRole(Long accountId, String roleName) {
        Account account = accountRepo.findById(accountId).orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        AccountRole accountRole = accountRoleRepo.findByAccount_AccountIdAndRole_Name(accountId, roleName);
        if (accountRole == null)
            throw new AppException(ErrorCode.ROLE_NOT_ASSIGNED_TO_ACCOUNT);

        accountRoleRepo.delete(accountRole);

        if (account.getAccountRoles() != null)
            account.getAccountRoles().remove(accountRole);

    }

    @Override
    @Transactional
    public void changeAvatar(Long accountId, MultipartFile avatarFile) {
        Account account = accountRepo.findById(accountId).orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        // Kiểm tra null an toàn trước khi check empty/blank
        String oldPath = account.getAvatarPath();
        if (oldPath != null && !oldPath.isBlank()) {
            minioService.deleteFile(oldPath);
        }

        // upload new file
        String objectName = minioService.uploadImage(avatarFile, ConstantUtils.Avatar);

        // save object name
        account.setAvatarPath(objectName);
        accountRepo.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AccountRes> getAccounts(Pageable pageable) {
        Page<Account> accountPage = accountRepo.findAll(pageable);

        return pageMapper.toPageResponse(accountPage, account ->
                {
                    AccountRes res = accountMapper.toRes(account);

                    if (account.getAvatarPath() != null && !account.getAvatarPath().isBlank()) {
                        account.setAvatarUrl(minioService.getPresignedUrl(account.getAvatarPath()));
                    }

                    return res;
                }
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AccountRes getAccountById(Long accountId) {
        Account account = accountRepo.findById(accountId).
                orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        if (account.getAvatarPath() != null && !account.getAvatarPath().isBlank()) {
            account.setAvatarUrl(minioService.getPresignedUrl(account.getAvatarPath()));
        }

        return accountMapper.toRes(account);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDetailsRes getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        Account account = accountRepo.findByEmail(username)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        return accountMapper.toDetailsRes(account);
    }
}
