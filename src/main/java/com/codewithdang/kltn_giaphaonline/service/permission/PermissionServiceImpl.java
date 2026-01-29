package com.codewithdang.kltn_giaphaonline.service.permission;

import com.codewithdang.kltn_giaphaonline.dto.request.CreatePermissionReq;
import com.codewithdang.kltn_giaphaonline.entity.Permission;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.repo.PermissionRepo;
import com.codewithdang.kltn_giaphaonline.repo.RolePermissionRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {
    PermissionRepo permissionRepo;
    RolePermissionRepo rolePermissionRepo;

    @Override
    @Transactional
    public Permission createPermission(CreatePermissionReq req) {
        if (permissionRepo.existsById(req.name()))
            throw new AppException(ErrorCode.PERMISSION_EXISTED);

        Permission permission = Permission.builder()
                .name(req.name())
                .description(req.description())
                .build();

        Permission saved =  permissionRepo.save(permission);
        log.info("Created permission with name {}", saved.getName());
        return saved;
    }

    @Override
    @Transactional
    public Permission updatePermission(CreatePermissionReq req) {
        Permission permission = permissionRepo.findById(req.name())
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));
        permission.setDescription(req.description());

        return permissionRepo.save(permission);
    }

    @Override
    @Transactional
    public void deletePermission(String permissionName) {
        Permission permission = permissionRepo.findById(permissionName)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));

        boolean isUse = rolePermissionRepo.existsByPermission_Name(permissionName);
        if (isUse)
            throw new AppException(ErrorCode.PERMISSION_IS_ALREADY_USED);

        permissionRepo.delete(permission);
        log.info("Deleted permission: {}", permissionName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> getPermissions() {
        return permissionRepo.findAll();
    }

    @Override
    public Permission getPermission(String permissionName) {
        return permissionRepo.findById(permissionName)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));
    }

}
