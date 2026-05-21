package com.codewithdang.kltn_giaphaonline.config.app;

import com.codewithdang.kltn_giaphaonline.dto.request.CreatePermissionReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CreateRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateRoleReq;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.AccountRole;
import com.codewithdang.kltn_giaphaonline.entity.AccountRoleId;
import com.codewithdang.kltn_giaphaonline.entity.Role;
import com.codewithdang.kltn_giaphaonline.enums.AccountStatus;
import com.codewithdang.kltn_giaphaonline.enums.PermissionEnums;
import com.codewithdang.kltn_giaphaonline.enums.RoleEnums;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.AccountRoleRepo;
import com.codewithdang.kltn_giaphaonline.repo.PermissionRepo;
import com.codewithdang.kltn_giaphaonline.repo.RoleRepo;
import com.codewithdang.kltn_giaphaonline.service.account.AccountService;
import com.codewithdang.kltn_giaphaonline.service.permission.PermissionService;
import com.codewithdang.kltn_giaphaonline.service.role.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppInitConfig {
    PasswordEncoder passwordEncoder;


    @Bean
    ApplicationRunner initApplicationRunner(AccountService accountService, PermissionService permissionService, RoleService roleService,
                                            AccountRepo accountRepo, RoleRepo roleRepo, PermissionRepo permissionRepo, AccountRoleRepo accountRoleRepo
    ) {
        return args -> {

            // 1. sync permission - thêm mới nếu chưa có
            for (PermissionEnums permissionEnums : PermissionEnums.values()) {
                if (!permissionRepo.existsById(permissionEnums.name())) {
                    permissionService.createPermission(
                            CreatePermissionReq.builder()
                                    .name(permissionEnums.name())
                                    .scopeType(permissionEnums.getScopeType().name())
                                    .description(permissionEnums.name())
                                    .build()
                    );
                    log.info("[INIT] Created permission: {}", permissionEnums.name());
                }
            }

            // 2. sync role - tạo mới nếu chưa có, cập nhật permission nếu thiếu
            for (RoleEnums roleEnums : RoleEnums.values()) {
                if (!roleRepo.existsById(roleEnums.name())) {
                    // tạo role mới
                    roleService.createRole(
                            CreateRoleReq.builder()
                                    .name(roleEnums.name())
                                    .scopeType(roleEnums.getScopeType().name())
                                    .description(roleEnums.getDescription())
                                    .build()
                    );
                    log.info("[INIT] Created role: {}", roleEnums.name());
                }

                // luôn sync permission vào role (thêm những cái còn thiếu)
                Set<String> expectedPerms = roleEnums.getPermissionEnums().stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet());

                roleService.addPermissionToRole(
                        roleEnums.name(),
                        UpdateRoleReq.builder()
                                .permissions(expectedPerms)
                                .description(roleEnums.getDescription())
                                .build()
                );
            }

            // create account
            // USER
//            if (!accountRepo.existsByEmail("user@gmail.com")) {
//
//                Account accountUser = Account.builder()
//                        .fullName("USER")
//                        .email("user@gmail.com")
//                        .passwordHash(passwordEncoder.encode("user@123"))
//                        .accountStatus(AccountStatus.ACTIVE)
//                        .build();
//
//                accountUser = accountRepo.save(accountUser);
//                Role roleUser = roleRepo.findById(RoleEnums.FAMILY_ADMIN.name())
//                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
//
//
//                AccountRole accountRoleUser = AccountRole.builder()
//                        .id(new AccountRoleId(accountUser.getAccountId(), roleUser.getName()))
//                        .account(accountUser)
//                        .role(roleUser)
//                        .build();
//
//                accountRoleRepo.save(accountRoleUser);
//                accountUser.setAccountRoles(new HashSet<>(Set.of(accountRoleUser)));
//            }

            // ADMIN
            if (!accountRepo.existsByEmail("admin@gmail.com")) {

                Account account = Account.builder()
                        .fullName("ADMIN")
                        .email("admin@gmail.com")
                        .passwordHash(passwordEncoder.encode("Admin@123"))
                        .accountStatus(AccountStatus.ACTIVE)
                        .build();

                account = accountRepo.save(account);

                Role role = roleRepo.findById(RoleEnums.SYSTEM_ADMIN.name())
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));


                AccountRole accountRole = AccountRole.builder()
                        .id(new AccountRoleId(account.getAccountId(), role.getName()))
                        .account(account)
                        .role(role)
                        .build();

                accountRoleRepo.save(accountRole);
                account.setAccountRoles(new HashSet<>(Set.of(accountRole)));
            }
        };
    }


}
