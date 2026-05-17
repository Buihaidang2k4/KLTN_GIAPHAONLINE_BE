package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.RoleRes;
import com.codewithdang.kltn_giaphaonline.mapper.RoleMapper;
import com.codewithdang.kltn_giaphaonline.service.role.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    ResponseEntity<ApiResponse<PageResponse<RoleRes>>> getAll(
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_ROLES_SUCCESS",
                roleService.getAll(pageable)));
    }

    @PostMapping
    ResponseEntity<ApiResponse<RoleRes>> createRole(@Valid @RequestBody CreateRoleReq req) {
        return ResponseEntity.status(201).body(ApiResponse.success(201, "CREATE_ROLE_SUCCESS",
                roleMapper.toRes(roleService.createRole(req))));
    }

    @PutMapping("/add-permission/{roleName}")
    ResponseEntity<ApiResponse<RoleRes>> addPermission(
            @PathVariable String roleName,
            @Valid @RequestBody UpdateRoleReq req) {
        return ResponseEntity.ok(ApiResponse.success(200, "ADD_PERMISSION_TO_ROLE_SUCCESS",
                roleMapper.toRes(roleService.addPermissionToRole(roleName, req))));
    }

    @DeleteMapping("/remove-permission/{roleName}")
    ResponseEntity<ApiResponse<Void>> removePermissions(
            @PathVariable String roleName,
            @Valid @RequestBody UpdateRoleReq req) {
        roleService.removePermissionFromRole(roleName, req);
        return ResponseEntity.ok(ApiResponse.success(200, "REMOVE_PERMISSION_FROM_ROLE_SUCCESS", null));
    }

    @DeleteMapping("/{roleName}")
    ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable String roleName) {
        roleService.deleteRole(roleName);
        return ResponseEntity.ok(ApiResponse.success(200, "DELETE_ROLE_SUCCESS", null));
    }

    @GetMapping("/me")
    ResponseEntity<ApiResponse<List<RoleRes>>> getRoleByCurrentAccount() {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_ROLE_BY_CURRENT_ACCOUNT_SUCCESS",
                roleService.getRoleByCurrentAccount()));
    }

    @GetMapping("/me/family/{familyId}")
    ResponseEntity<ApiResponse<List<RoleRes>>> getRoleByFamilyId(@PathVariable Long familyId) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_ROLE_BY_FAMILY_SUCCESS",
                roleService.getCurrentRoleByFamilyId(familyId)));
    }

    @GetMapping("/me/is-system")
    ResponseEntity<ApiResponse<Boolean>> isSystemAccount() {
        return ResponseEntity.ok(ApiResponse.success(200, "CHECK_SYSTEM_ACCOUNT_SUCCESS",
                roleService.isSystemAccount()));
    }
}
