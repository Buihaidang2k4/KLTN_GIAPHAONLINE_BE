package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Role;
import com.codewithdang.kltn_giaphaonline.enums.RoleScopeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, String> {
    boolean existsRolesByName(String name);

    Optional<Role> findByName(String roleName);

    @Query("SELECT r FROM Role r WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:scopeType IS NULL OR r.scopeType = :scopeType)")
    Page<Role> searchByNameAndScope(@Param("keyword") String keyword,
                                    @Param("scopeType") RoleScopeType scopeType,
                                    Pageable pageable);
}
