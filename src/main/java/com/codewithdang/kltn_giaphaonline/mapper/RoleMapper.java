package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.PermissionRes;
import com.codewithdang.kltn_giaphaonline.dto.response.RoleRes;
import com.codewithdang.kltn_giaphaonline.entity.Permission;
import com.codewithdang.kltn_giaphaonline.entity.Role;
import com.codewithdang.kltn_giaphaonline.entity.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
    Role toEntity(CreateRoleReq req);

    @Mapping(source = "rolePermissions", target = "permissions")
    RoleRes toRes(Role role);

    default PermissionRes mapRolePermissionToPermissionRes(RolePermission rolePermission) {
        if (rolePermission == null || rolePermission.getPermission() == null) {
            return null;
        }

        Permission p = rolePermission.getPermission();
        return new PermissionRes(p.getName(), p.getDescription());
    }
}
