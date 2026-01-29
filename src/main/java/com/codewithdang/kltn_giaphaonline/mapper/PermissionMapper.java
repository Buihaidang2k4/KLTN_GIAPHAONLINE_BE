package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.CreatePermissionReq;
import com.codewithdang.kltn_giaphaonline.dto.response.PermissionRes;
import com.codewithdang.kltn_giaphaonline.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toEntity(CreatePermissionReq req);

    PermissionRes toResponse(Permission permission);

}
