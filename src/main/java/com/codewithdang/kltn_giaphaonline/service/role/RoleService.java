package com.codewithdang.kltn_giaphaonline.service.role;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.RoleRes;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Role;
import com.codewithdang.kltn_giaphaonline.enums.RoleEnums;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    Role createRole(CreateRoleReq request);

    void assignRoleToAccount(Account account, RoleEnums roleEnums);

    Role addPermissionToRole(String roleName, UpdateRoleReq req);

    void removePermissionFromRole(String roleName, UpdateRoleReq req);

    void deleteRole(String roleName);

    List<Role> getAllRoles();

    List<RoleRes> getRoleByCurrentAccount();

    List<RoleRes> getCurrentRoleByFamilyId(Long familyId);

    PageResponse<RoleRes> getAll(String keyword, String scopeType, Pageable pageable);

    boolean isSystemAccount();
}
