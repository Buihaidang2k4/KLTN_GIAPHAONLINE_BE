package com.codewithdang.kltn_giaphaonline.controller;


import com.codewithdang.kltn_giaphaonline.dto.request.CreatePermissionReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PermissionRes;
import com.codewithdang.kltn_giaphaonline.entity.Permission;
import com.codewithdang.kltn_giaphaonline.mapper.PermissionMapper;
import com.codewithdang.kltn_giaphaonline.service.permission.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Permission Management")
public class PermissionController {
    PermissionService permissionService;
    PermissionMapper permissionMapper;

    @GetMapping
    ResponseEntity<ApiResponse<List<PermissionRes>>> getAllPermissions() {
        List<Permission> permissions = permissionService.getPermissions();
        List<PermissionRes> permissionRes = permissions.stream().map(permissionMapper::toResponse).toList();

        return ResponseEntity.ok(new ApiResponse<>(200, "permissions", permissionRes));
    }

    @GetMapping("/{permissionName}")
    ResponseEntity<ApiResponse<PermissionRes>> getPermission(@PathVariable String permissionName) {

        return ResponseEntity.ok(new ApiResponse<>(200, "permission", permissionMapper.toResponse(permissionService.getPermission(permissionName))));
    }

    @PostMapping
    ResponseEntity<ApiResponse<PermissionRes>> createPermission(@RequestBody CreatePermissionReq req) {
        return ResponseEntity.ok(new ApiResponse<>(201, "permission", permissionMapper.toResponse(permissionService.createPermission(req))));
    }

    @PutMapping
    ResponseEntity<ApiResponse<PermissionRes>> updatePermission(@RequestBody CreatePermissionReq req) {
        return ResponseEntity
                .ok(new ApiResponse<>(200,
                        "update desc successfully",
                        permissionMapper.toResponse(permissionService.updatePermission(req))));
    }

    @DeleteMapping("/{permissionName}")
    ResponseEntity<ApiResponse<?>> deletePermission(@PathVariable String permissionName) {
        permissionService.deletePermission(permissionName);
        return ResponseEntity.ok(new ApiResponse<>(204, "Deleted permissionName: " + permissionName, null));
    }


}
