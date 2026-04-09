package com.codewithdang.kltn_giaphaonline.service.family_invitation;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateFamilyInvitationReq;
import com.codewithdang.kltn_giaphaonline.dto.response.InviteMemberRes;

public interface FamilyInvitationService {
    InviteMemberRes inviteMember(Long familyId, Long inviterAccountId, CreateFamilyInvitationReq createFamilyInvitationReq);

    void acceptInvitation(String token, Long accountId);

    void rejectInvitation(String inviteToken, Long currentAccountId);
}
