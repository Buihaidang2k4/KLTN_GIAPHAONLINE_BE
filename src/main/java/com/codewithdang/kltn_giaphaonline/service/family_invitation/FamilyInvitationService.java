package com.codewithdang.kltn_giaphaonline.service.family_invitation;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateFamilyInvitationReq;
import com.codewithdang.kltn_giaphaonline.dto.response.InviteInvitationMemberRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FamilyInvitationService {
    PageResponse<InviteInvitationMemberRes> getMyInvitationsSent(Pageable pageable);

    PageResponse<InviteInvitationMemberRes> getMyInvitationsReceived(Pageable pageable);

    InviteInvitationMemberRes inviteMember(Long familyId, CreateFamilyInvitationReq createFamilyInvitationReq);

    void acceptInvitation(String token);

    void acceptInvitationForNewAccount(String token, com.codewithdang.kltn_giaphaonline.entity.Account account);

    void rejectInvitation(String inviteToken);

    void cancelInvitation(Long invitationId);
}
