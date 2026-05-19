package com.codewithdang.kltn_giaphaonline.service.permission;

import com.codewithdang.kltn_giaphaonline.dto.request.CreatePermissionReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdatePermissionReq;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PermissionRes;
import com.codewithdang.kltn_giaphaonline.entity.Permission;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PermissionService {
    Permission createPermission(CreatePermissionReq req);

    Permission updatePermission(String permissionName, UpdatePermissionReq req);

    void deletePermission(String permissionName);

    List<Permission> getPermissions();

    PageResponse<PermissionRes> getAllPaged(String keyword, String scopeType, Pageable pageable);

    List<PermissionRes> getAllByScopeType(String scopeType);

    Permission getPermission(String permissionName);
}
