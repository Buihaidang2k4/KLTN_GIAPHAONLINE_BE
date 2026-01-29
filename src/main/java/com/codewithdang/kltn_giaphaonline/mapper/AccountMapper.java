package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.ChangePasswordAccountReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CreateAccountReq;
import com.codewithdang.kltn_giaphaonline.dto.response.AccountRes;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toEntity(CreateAccountReq req);

    AccountRes toRes(Account account);

    void updateEntity(@MappingTarget Account account, ChangePasswordAccountReq req);


}
