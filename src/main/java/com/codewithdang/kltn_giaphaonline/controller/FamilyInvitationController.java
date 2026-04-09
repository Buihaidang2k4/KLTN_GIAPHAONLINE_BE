package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateFamilyInvitationReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.InviteMemberRes;
import com.codewithdang.kltn_giaphaonline.service.family_invitation.FamilyInvitationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/families-invitations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FamilyInvitationController {

    FamilyInvitationService familyInvitationService;

    @PostMapping("/{familyId}/invitations")
    public ResponseEntity<ApiResponse<InviteMemberRes>> inviteMember(
            @PathVariable Long familyId,
            @RequestParam Long inviterAccountId,
            @RequestBody @Valid CreateFamilyInvitationReq request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "INVITE_MEMBER_SUCCESS",
                        familyInvitationService.inviteMember(familyId, inviterAccountId, request)
                )
        );
    }

    @PostMapping("/invitations/{token}/accept")
    public ResponseEntity<ApiResponse<Void>> acceptInvitation(
            @PathVariable String token,
            @RequestParam Long accountId
    ) {
        familyInvitationService.acceptInvitation(token, accountId);

        return ResponseEntity.ok(
                ApiResponse.success(200, "ACCEPT_INVITATION_SUCCESS", null)
        );
    }

    @PostMapping("/invitations/{token}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectInvitation(
            @PathVariable String token,
            @RequestParam Long accountId
    ) {
        familyInvitationService.rejectInvitation(token, accountId);

        return ResponseEntity.ok(
                ApiResponse.success(200, "REJECT_INVITATION_SUCCESS", null)
        );
    }
}