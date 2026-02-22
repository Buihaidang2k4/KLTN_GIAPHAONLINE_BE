package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.ChangePasswordAccountReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CreateAccountReq;
import com.codewithdang.kltn_giaphaonline.dto.response.AccountRes;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.AccountRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",uses = RoleMapper.class)
public interface AccountMapper {

    Account toEntity(CreateAccountReq req);

    @Mapping(target = "roles",source = "accountRoles")
    AccountRes toRes(Account account);

    void updateEntity(@MappingTarget Account account, ChangePasswordAccountReq req);

    default List<String> mapRoles(Set<AccountRole> accountRoles) {
        if(accountRoles==null){
            return null;
        }

        return accountRoles.stream()
                .map(accountRole -> accountRole.getRole().getName())
                .toList();
    }
}
