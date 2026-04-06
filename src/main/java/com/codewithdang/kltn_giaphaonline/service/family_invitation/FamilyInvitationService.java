package com.codewithdang.kltn_giaphaonline.service.family_invitation;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateFamilyInvitationReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyMemberRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.InviteMemberRes;

public interface FamilyInvitationService {
    InviteMemberRes inviteMember(Long familyId, Long inviterAccountId, CreateFamilyInvitationReq createFamilyInvitationReq);

    void acceptInvitation(String token);

    void rejectInvitation(String inviteToken, Long currentAccountId);

    void updateMemberRole(Long familyId, Long targetAccountId, UpdateFamilyMemberRoleReq request, Long actorAccountId);

    void removeMember(Long familyId, Long targetAccountId, Long actorAccountId);
}
