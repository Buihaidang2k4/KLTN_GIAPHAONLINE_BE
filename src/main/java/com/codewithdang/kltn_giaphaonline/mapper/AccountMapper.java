package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.ChangePasswordAccountReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CreateAccountReq;
import com.codewithdang.kltn_giaphaonline.dto.response.AccountDetailsRes;
import com.codewithdang.kltn_giaphaonline.dto.response.AccountRes;
import com.codewithdang.kltn_giaphaonline.dto.response.RoleRes;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.AccountRole;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public abstract class AccountMapper {

    @Autowired
    protected RoleMapper roleMapper;

    public abstract Account toEntity(CreateAccountReq req);

    @Mapping(target = "roles", source = "accountRoles", qualifiedByName = "mapAccountRoleToString")
    public abstract AccountRes toRes(Account account);

    @Mapping(target = "families", source = "ownedFamilies")
    @Mapping(target = "roles", source = "accountRoles", qualifiedByName = "mapAccountRoles")
    public abstract AccountDetailsRes toDetailsRes(Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntity(@MappingTarget Account account, ChangePasswordAccountReq req);

    @Named("mapAccountRoles")
    protected Set<RoleRes> mapAccountRoles(Set<AccountRole> accountRoles) {
        if (accountRoles == null || accountRoles.isEmpty()) {
            return Collections.emptySet();
        }

        return accountRoles.stream()
                .map(AccountRole::getRole)
                .filter(Objects::nonNull)
                .map(roleMapper::toRes)
                .collect(Collectors.toSet());
    }

    @Named("mapAccountRoleToString")
    protected List<String> mapAccountRoleToString(Set<AccountRole> accountRoles) {
        if (accountRoles == null || accountRoles.isEmpty()) {
            return Collections.emptyList();
        }

        return accountRoles.stream()
                .map(AccountRole::getRole)
                .filter(Objects::nonNull)
                .map(role -> role.getName())
                .collect(Collectors.toList());
    }
}