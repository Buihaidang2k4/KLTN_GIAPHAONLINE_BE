package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.CreatePermissionReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdatePermissionReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PermissionRes;
import com.codewithdang.kltn_giaphaonline.entity.Permission;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
import com.codewithdang.kltn_giaphaonline.mapper.PermissionMapper;
import com.codewithdang.kltn_giaphaonline.service.permission.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPath.Permission.BASE)
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Permission Management")
public class PermissionController {
    PermissionService permissionService;
    PermissionMapper permissionMapper;

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping
    ResponseEntity<ApiResponse<PageResponse<PermissionRes>>> getAllPermissions(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) String scopeType,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Permission.GET_ALL_PERMISSIONS_SUCCESS,
                        permissionService.getAllPaged(keyword, scopeType, pageable))
        );
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Permission.LIST)
    ResponseEntity<ApiResponse<List<PermissionRes>>> getAllList(
            @RequestParam(required = false) String scopeType) {
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Permission.GET_ALL_PERMISSIONS_LIST_SUCCESS,
                        permissionService.getAllByScopeType(scopeType))
        );
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Permission.BY_NAME)
    ResponseEntity<ApiResponse<PermissionRes>> getPermission(@PathVariable String permissionName) {
        PermissionRes res = permissionMapper.toResponse(permissionService.getPermission(permissionName));
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Permission.GET_PERMISSION_SUCCESS, res)
        );
    }

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping
    ResponseEntity<ApiResponse<PermissionRes>> createPermission(@Valid @RequestBody CreatePermissionReq req) {
        PermissionRes res = permissionMapper.toResponse(permissionService.createPermission(req));
        return ResponseEntity.status(201).body(
                ApiResponse.success(201, MessageConstantsVi.Permission.CREATE_PERMISSION_SUCCESS, res)
        );
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PutMapping(ApiPath.Permission.BY_NAME)
    ResponseEntity<ApiResponse<PermissionRes>> updatePermission(
            @PathVariable String permissionName,
            @Valid @RequestBody UpdatePermissionReq req) {
        PermissionRes res = permissionMapper.toResponse(permissionService.updatePermission(permissionName, req));
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Permission.UPDATE_PERMISSION_SUCCESS, res)
        );
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.Permission.BY_NAME)
    ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable String permissionName) {
        permissionService.deletePermission(permissionName);
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.Permission.DELETE_PERMISSION_SUCCESS, null)
        );
    }
}