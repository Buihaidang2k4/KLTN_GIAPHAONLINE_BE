package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.RolePermission;
import com.codewithdang.kltn_giaphaonline.entity.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RolePermissionRepo extends JpaRepository<RolePermission, RolePermissionId> {
    boolean existsByPermission_Name(String permissionName);

    List<RolePermission> findByRole_Name(String roleName);

    boolean existsByPermission_NameAndRole_Name(String name, String roleName);

    List<RolePermission> findByRole_NameAndPermission_NameIn(String roleName, Set<String> permissions);
}
