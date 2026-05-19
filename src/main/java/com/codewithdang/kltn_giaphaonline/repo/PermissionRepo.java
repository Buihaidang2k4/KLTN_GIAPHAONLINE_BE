package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Permission;
import com.codewithdang.kltn_giaphaonline.enums.RoleScopeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, String> {

    @Query("SELECT p FROM Permission p WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:scopeType IS NULL OR p.scopeType = :scopeType)")
    Page<Permission> searchByNameAndScope(@Param("keyword") String keyword,
                                          @Param("scopeType") RoleScopeType scopeType,
                                          Pageable pageable);

    @Query("SELECT p FROM Permission p WHERE :scopeType IS NULL OR p.scopeType = :scopeType ORDER BY p.name ASC")
    List<Permission> findAllByScope(@Param("scopeType") RoleScopeType scopeType);
}
