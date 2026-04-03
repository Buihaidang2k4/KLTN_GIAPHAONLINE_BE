package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.CreatePermissionReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PermissionRes;
import com.codewithdang.kltn_giaphaonline.entity.Permission;
import com.codewithdang.kltn_giaphaonline.mapper.PermissionMapper;
import com.codewithdang.kltn_giaphaonline.service.permission.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/permissions")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Permission Management")
public class PermissionController {
    PermissionService permissionService;
    PermissionMapper permissionMapper;

    @GetMapping
    ResponseEntity<ApiResponse<List<PermissionRes>>> getAllPermissions() {
        List<PermissionRes> permissions = permissionService.getPermissions()
                .stream()
                .map(permissionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(
                ApiResponse.success(200, "GET_ALL_PERMISSIONS_SUCCESS", permissions)
        );
    }

    @GetMapping("/{permissionName}")
    ResponseEntity<ApiResponse<PermissionRes>> getPermission(@PathVariable String permissionName) {
        PermissionRes res = permissionMapper.toResponse(permissionService.getPermission(permissionName));
        return ResponseEntity.ok(
                ApiResponse.success(200, "GET_PERMISSION_SUCCESS", res)
        );
    }

    @PostMapping
    ResponseEntity<ApiResponse<PermissionRes>> createPermission(@Valid @RequestBody CreatePermissionReq req) {
        PermissionRes res = permissionMapper.toResponse(permissionService.createPermission(req));
        return ResponseEntity.status(201).body(
                ApiResponse.success(201, "CREATE_PERMISSION_SUCCESS", res)
        );
    }

    @PutMapping
    ResponseEntity<ApiResponse<PermissionRes>> updatePermission(@Valid @RequestBody CreatePermissionReq req) {
        PermissionRes res = permissionMapper.toResponse(permissionService.updatePermission(req));
        return ResponseEntity.ok(
                ApiResponse.success(200, "UPDATE_PERMISSION_SUCCESS", res)
        );
    }

    @DeleteMapping("/{permissionName}")
    ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable String permissionName) {
        permissionService.deletePermission(permissionName);
        return ResponseEntity.ok(
                ApiResponse.success(200, "DELETE_PERMISSION_SUCCESS", null)
        );
    }
}