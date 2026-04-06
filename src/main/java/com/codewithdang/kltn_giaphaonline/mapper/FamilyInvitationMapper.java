package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateFamilyInvitationReq;
import com.codewithdang.kltn_giaphaonline.dto.response.InviteMemberRes;
import com.codewithdang.kltn_giaphaonline.entity.FamilyInvitation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FamilyInvitationMapper {
    FamilyInvitation toEntity(CreateFamilyInvitationReq createFamilyInvitationReq);

    InviteMemberRes toRes(FamilyInvitation familyInvitation);
}
