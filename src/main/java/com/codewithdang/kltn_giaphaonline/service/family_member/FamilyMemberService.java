package com.codewithdang.kltn_giaphaonline.service.family_member;

import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyMemberRoleReq;
import com.codewithdang.kltn_giaphaonline.entity.FamilyMember;

public interface FamilyMemberService {
    FamilyMember addMember(Long familyId, Long accountId, String roleName);

    void updateMemberRole(Long familyId, Long targetAccountId, UpdateFamilyMemberRoleReq memberRoleReq, Long actorAccountId);

    void removeMember(Long familyId, Long targetAccountId, Long actorAccountId);

    boolean isActiveMember(Long familyId, Long accountId);
}
