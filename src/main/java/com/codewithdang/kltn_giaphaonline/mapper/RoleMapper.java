package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.PermissionRes;
import com.codewithdang.kltn_giaphaonline.dto.response.RoleRes;
import com.codewithdang.kltn_giaphaonline.entity.Role;
import com.codewithdang.kltn_giaphaonline.entity.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class RoleMapper {
    @Autowired
    protected PermissionMapper permissionMapper;

    public abstract Role toEntity(CreateRoleReq req);

    @Mapping(target = "permissions", source = "rolePermissions", qualifiedByName = "mapRolePermissions")
    public abstract RoleRes toRes(Role role);

    @Named("mapRolePermissions")
    protected Set<PermissionRes> mapRolePermissions(Set<RolePermission> rolePermissions) {
        if (rolePermissions == null || rolePermissions.isEmpty())
            return Collections.emptySet();

        return rolePermissions.stream()
                .map(RolePermission::getPermission)
                .filter(permission -> permission != null)
                .map(permissionMapper::toResponse)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
