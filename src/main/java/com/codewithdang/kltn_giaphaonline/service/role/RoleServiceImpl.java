package com.codewithdang.kltn_giaphaonline.service.role;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateRoleReq;
import com.codewithdang.kltn_giaphaonline.entity.*;
import com.codewithdang.kltn_giaphaonline.enums.RoleEnums;
import com.codewithdang.kltn_giaphaonline.enums.RoleScopeType;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.repo.AccountRoleRepo;
import com.codewithdang.kltn_giaphaonline.repo.PermissionRepo;
import com.codewithdang.kltn_giaphaonline.repo.RolePermissionRepo;
import com.codewithdang.kltn_giaphaonline.repo.RoleRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepo roleRepository;
    AccountRoleRepo accountRoleRepo;
    PermissionRepo permissionRepo;
    RolePermissionRepo rolePermissionRepo;

    @Override
    @Transactional
//    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public Role createRole(CreateRoleReq request) {
        if (roleRepository.existsRolesByName(request.name()))
            throw new AppException(ErrorCode.ROLE_EXISTED);

        if (request.scopeType() == null || request.scopeType().isBlank()) {
            throw new AppException(ErrorCode.SCOPE_TYPE_IS_NULL);
        }

        Role role = Role.builder()
                .name(request.name())
                .scopeType(RoleScopeType.valueOf(request.scopeType().trim().toUpperCase()))
                .description(request.description())
                .build();

        return roleRepository.save(role);
    }

    @Override
    public void assignRoleToAccount(Account account, RoleEnums roleEnums) {
        Role role = roleRepository.findById(roleEnums.name())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        AccountRole accountRole = AccountRole.builder()
                .id(
                        new AccountRoleId(account.getAccountId(), role.getName())
                )
                .account(account)
                .role(role)
                .build();

        accountRoleRepo.save(accountRole);
        account.setAccountRoles(new HashSet<>(Set.of(accountRole)));
        account.getAccountRoles().add(accountRole);
    }

    @Override
    @Transactional
    public Role addPermissionToRole(String roleName, UpdateRoleReq request) {
        Role role = roleRepository.findById(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        if (request.description() != null)
            role.setDescription(request.description());

        List<Permission> foundPermissions = permissionRepo.findAllById(request.permissions());
        if (foundPermissions.size() != request.permissions().size()) {
            log.error("Một số Permission trong danh sách không tồn tại");
            throw new AppException(ErrorCode.PERMISSION_NOT_EXISTED);
        }

        boolean hasDifferentScope = foundPermissions.stream().
                anyMatch(permission -> permission.getScopeType() != role.getScopeType());

        if (hasDifferentScope) {
            log.error("Có permission khác scope với role. Role: {}, scope: {}", role.getName(), role.getScopeType());
            throw new AppException(ErrorCode.ROLE_PERMISSION_SCOPE_MISMATCH);
        }

        Set<String> existingPermissionNames = rolePermissionRepo.findByRole_Name(roleName).
                stream()
                .map(rp -> rp.getPermission().getName())
                .collect(Collectors.toSet());

        List<RolePermission> newRolePermission = foundPermissions.stream()
                .filter(p -> !existingPermissionNames.contains(p.getName()))
                .map(p -> {
                    RolePermissionId compositeId = new RolePermissionId(role.getName(), p.getName());

                    return RolePermission.builder()
                            .id(compositeId)
                            .role(role)
                            .permission(p)
                            .build();
                }).toList();

        if (!newRolePermission.isEmpty())
            rolePermissionRepo.saveAll(newRolePermission);

        return roleRepository.save(role);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public void removePermissionFromRole(String roleName, UpdateRoleReq req) {
        List<RolePermission> toRemove = rolePermissionRepo.findByRole_NameAndPermission_NameIn(roleName, req.permissions());

        if (toRemove.isEmpty()) {
            throw new AppException(ErrorCode.PERMISSION_NOT_ASSIGNED_TO_ROLE);
        }

        rolePermissionRepo.deleteAllInBatch(toRemove);

        log.info("Đã gỡ {} quyền khỏi Role {}", toRemove.size(), roleName);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public void deleteRole(String roleName) {
        Role role = roleRepository.findById(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        boolean roleUsed = accountRoleRepo.countByRole_Name(role.getName()) > 0;
        if (roleUsed)
            throw new AppException(ErrorCode.ROLE_IS_ALREADY_USED);

        roleRepository.delete(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
