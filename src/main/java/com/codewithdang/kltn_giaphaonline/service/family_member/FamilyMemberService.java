package com.codewithdang.kltn_giaphaonline.service.family_member;

import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyMemberRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyMemberRes;
import com.codewithdang.kltn_giaphaonline.entity.FamilyMember;

import java.util.List;

public interface FamilyMemberService {
    List<FamilyMemberRes> getFamilyMemberByFamilyId(Long familyId);

    void assignFamilyAdmin(Long familyId, Long accountId);

    FamilyMember addMember(Long familyId, Long accountId, String roleName);

    void updateMemberRole(Long familyId, Long targetAccountId, UpdateFamilyMemberRoleReq memberRoleReq);

    void removeMember(Long familyId, Long targetAccountId);

    boolean isActiveMember(Long familyId, Long accountId);
}
