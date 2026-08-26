package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.CreateRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.RoleRes;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
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
@RequestMapping(ApiPath.Role.BASE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Roles Management")
public class RoleController {

    RoleService roleService;
    RoleMapper roleMapper;

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping
    ResponseEntity<ApiResponse<PageResponse<RoleRes>>> getAllByPaged(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) String scopeType,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Role.GET_ROLES_SUCCESS,
                roleService.getAll(keyword, scopeType, pageable)));
    }

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping
    ResponseEntity<ApiResponse<RoleRes>> createRole(@Valid @RequestBody CreateRoleReq req) {
        return ResponseEntity.status(201).body(ApiResponse.success(201, MessageConstantsVi.Role.CREATE_ROLE_SUCCESS,
                roleMapper.toRes(roleService.createRole(req))));
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PutMapping(ApiPath.Role.ADD_PERMISSION)
    ResponseEntity<ApiResponse<RoleRes>> addPermission(
            @PathVariable String roleName,
            @Valid @RequestBody UpdateRoleReq req) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Role.ADD_PERMISSION_TO_ROLE_SUCCESS,
                roleMapper.toRes(roleService.addPermissionToRole(roleName, req))));
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.Role.REMOVE_PERMISSION)
    ResponseEntity<ApiResponse<Void>> removePermissions(
            @PathVariable String roleName,
            @Valid @RequestBody UpdateRoleReq req) {
        roleService.removePermissionFromRole(roleName, req);
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Role.REMOVE_PERMISSION_FROM_ROLE_SUCCESS, null));
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.Role.BY_NAME)
    ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable String roleName) {
        roleService.deleteRole(roleName);
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Role.DELETE_ROLE_SUCCESS, null));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Role.ME)
    ResponseEntity<ApiResponse<List<RoleRes>>> getRoleByCurrentAccount() {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Role.GET_ROLE_BY_CURRENT_ACCOUNT_SUCCESS,
                roleService.getRoleByCurrentAccount()));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Role.ME_FAMILY)
    ResponseEntity<ApiResponse<List<RoleRes>>> getRoleByFamilyId(@PathVariable Long familyId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Role.GET_ROLE_BY_FAMILY_SUCCESS,
                roleService.getCurrentRoleByFamilyId(familyId)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Role.ME_IS_SYSTEM)
    ResponseEntity<ApiResponse<Boolean>> isSystemAccount() {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Role.CHECK_SYSTEM_ACCOUNT_SUCCESS,
                roleService.isSystemAccount()));
    }
}
