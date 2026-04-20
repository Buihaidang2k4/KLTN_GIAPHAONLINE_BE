package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.response.FamilyMemberRes;
import com.codewithdang.kltn_giaphaonline.entity.FamilyMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FamilyMemberMapper {
    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "familyId", source = "family.familyId")
    @Mapping(target = "accountId", source = "account.accountId")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "email", source = "account.email")
    FamilyMemberRes toRes(FamilyMember familyMember);
}
