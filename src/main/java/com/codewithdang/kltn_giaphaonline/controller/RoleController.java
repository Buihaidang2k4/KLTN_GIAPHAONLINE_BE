package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.RoleRes;
import com.codewithdang.kltn_giaphaonline.mapper.RoleMapper;
import com.codewithdang.kltn_giaphaonline.service.role.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
        List<RoleRes> roleRes = roleService.getAllRoles()
                .stream()
                .map(roleMapper::toRes)
                .toList();
        return ResponseEntity.ok(
                ApiResponse.success(200, "GET_ROLES_SUCCESS", roleRes)
        );
    }

    @PostMapping
    ResponseEntity<ApiResponse<RoleRes>> createRole(@Valid @RequestBody CreateRoleReq req) {
        RoleRes res = roleMapper.toRes(roleService.createRole(req));
        return ResponseEntity.status(201).body(
                ApiResponse.success(201, "CREATE_ROLE_SUCCESS", res)
        );
    }

    @PutMapping("/add-permission/{roleName}")
    ResponseEntity<ApiResponse<RoleRes>> addPermission(@PathVariable String roleName, @Valid @RequestBody UpdateRoleReq req) {
        RoleRes res = roleMapper.toRes(roleService.addPermissionToRole(roleName, req));
        return ResponseEntity.ok(
                ApiResponse.success(200, "ADD_PERMISSION_TO_ROLE_SUCCESS", res)
        );
    }
    
    @DeleteMapping("/remove-permission/{roleName}")
    ResponseEntity<ApiResponse<Void>> removePermissions(@PathVariable String roleName, @Valid @RequestBody UpdateRoleReq req) {
        roleService.removePermissionFromRole(roleName, req);
        return ResponseEntity.ok(
                ApiResponse.success(200, "REMOVE_PERMISSION_FROM_ROLE_SUCCESS", null)
        );
    }

    @DeleteMapping("/{roleName}")
    ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable String roleName) {
        roleService.deleteRole(roleName);
        return ResponseEntity.ok(
                ApiResponse.success(200, "DELETE_ROLE_SUCCESS", null)
        );
    }
}