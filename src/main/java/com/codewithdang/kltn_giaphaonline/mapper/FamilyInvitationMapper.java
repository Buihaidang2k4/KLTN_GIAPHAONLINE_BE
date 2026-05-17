package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateFamilyInvitationReq;
import com.codewithdang.kltn_giaphaonline.dto.response.InviteInvitationMemberRes;
import com.codewithdang.kltn_giaphaonline.entity.FamilyInvitation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FamilyInvitationMapper {
    FamilyInvitation toEntity(CreateFamilyInvitationReq createFamilyInvitationReq);

    @Mapping(target = "familyName", source = "family.familyName")
    @Mapping(target = "invitedByAccountId", source = "invitedByAccount.accountId")
    @Mapping(target = "invitedByEmail", source = "invitedByAccount.email")
    @Mapping(target = "roleName", source = "role.name")
    InviteInvitationMemberRes toRes(FamilyInvitation familyInvitation);
}
