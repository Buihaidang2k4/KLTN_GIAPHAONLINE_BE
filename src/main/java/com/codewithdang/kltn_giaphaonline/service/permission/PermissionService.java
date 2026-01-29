package com.codewithdang.kltn_giaphaonline.service.permission;

import com.codewithdang.kltn_giaphaonline.dto.request.CreatePermissionReq;
import com.codewithdang.kltn_giaphaonline.entity.Permission;

import java.util.List;

public interface PermissionService {
    Permission createPermission(CreatePermissionReq req);

    Permission updatePermission(CreatePermissionReq req);

    void deletePermission(String permissionName);

    List<Permission> getPermissions();

    Permission getPermission(String permissionName);
}
