package com.codewithdang.kltn_giaphaonline.controller;


import com.codewithdang.kltn_giaphaonline.dto.request.CreateRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.RoleRes;
import com.codewithdang.kltn_giaphaonline.entity.Role;
import com.codewithdang.kltn_giaphaonline.mapper.RoleMapper;
import com.codewithdang.kltn_giaphaonline.service.role.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Roles Management")
public class RoleController {
    RoleService roleService;
    RoleMapper roleMapper;

    @GetMapping
    ResponseEntity<ApiResponse<List<RoleRes>>> getRoles() {
        List<Role> roles = roleService.getAllRoles();
        List<RoleRes> roleRes = roles.stream().map(roleMapper::toRes).toList();
        return ResponseEntity.ok(new ApiResponse<>(200, "ROLE", roleRes));
    }

    @PostMapping
    ResponseEntity<ApiResponse<RoleRes>> createRole(@RequestBody CreateRoleReq req) {
        return ResponseEntity.ok(new ApiResponse<>(201, "CREATE ROLE SUCCESSFULLY", roleMapper.toRes(roleService.createRole(req))));
    }

    @PutMapping("/add-permission/{roleName}")
    ResponseEntity<ApiResponse<RoleRes>> addPermission(@PathVariable String roleName, @RequestBody UpdateRoleReq req) {
        return ResponseEntity.ok(new ApiResponse<>(200, "ADD PERMISSION", roleMapper.toRes(roleService.addPermissionToRole(roleName, req))));
    }

    @DeleteMapping("/remove-permission/{roleName}")
    ResponseEntity<ApiResponse<?>> removePermissions(@PathVariable String roleName, @RequestBody UpdateRoleReq req) {
        roleService.removePermissionFromRole(roleName, req);
        return ResponseEntity.ok(new ApiResponse<>(204, "DELETE PERMISSION FROM ROLE: " + roleName, null));
    }

    @DeleteMapping("/{roleName}")
    ResponseEntity<ApiResponse<?>> deleteRole(@PathVariable String roleName) {
        roleService.deleteRole(roleName);
        return ResponseEntity.ok(new ApiResponse<>(204, "DELETE ROLE: {}" + roleName, null));
    }
}
