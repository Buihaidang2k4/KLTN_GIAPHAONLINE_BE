package com.codewithdang.kltn_giaphaonline.service.role;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateRoleReq;
import com.codewithdang.kltn_giaphaonline.entity.Role;

import java.util.List;

public interface RoleService {
    Role createRole(CreateRoleReq request);

    Role addPermissionToRole(String roleName, UpdateRoleReq req);

    void removePermissionFromRole(String roleName, UpdateRoleReq req);

    void deleteRole(String roleName);

    List<Role> getAllRoles();
}
