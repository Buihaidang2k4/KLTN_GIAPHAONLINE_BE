package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateFamilyInvitationReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.InviteInvitationMemberRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.service.family_invitation.FamilyInvitationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/family-invitations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FamilyInvitationController {

    FamilyInvitationService familyInvitationService;

    @GetMapping("/sent")
    public ResponseEntity<ApiResponse<PageResponse<InviteInvitationMemberRes>>> getSentInvitations(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable

    ) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "GET_MY_INVITATION_SENT_SUCCESS",
                        familyInvitationService.getMyInvitationsSent(pageable)));
    }


    @GetMapping("/received")
    public ResponseEntity<ApiResponse<PageResponse<InviteInvitationMemberRes>>> receivedInvitation(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "GET_MY_INVITATION_RECEIVED_SUCCESS",
                        familyInvitationService.getMyInvitationsReceived(pageable)));
    }


    @PostMapping("/{familyId}/invite")
    public ResponseEntity<ApiResponse<InviteInvitationMemberRes>> inviteMember(
            @PathVariable Long familyId,
            @RequestBody @Valid CreateFamilyInvitationReq request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "INVITE_MEMBER_SUCCESS",
                        familyInvitationService.inviteMember(familyId, request)
                )
        );
    }

    @PostMapping("/accept/{token}")
    public ResponseEntity<ApiResponse<Void>> acceptInvitation(
            @PathVariable String token
    ) {
        familyInvitationService.acceptInvitation(token);

        return ResponseEntity.ok(
                ApiResponse.success(200, "ACCEPT_INVITATION_SUCCESS", null)
        );
    }

    @PostMapping("/reject/{token}")
    public ResponseEntity<ApiResponse<Void>> rejectInvitation(
            @PathVariable String token
    ) {
        familyInvitationService.rejectInvitation(token);

        return ResponseEntity.ok(
                ApiResponse.success(200, "REJECT_INVITATION_SUCCESS", null)
        );
    }


    @PostMapping("/{invitationId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelInvitation(
            @PathVariable Long invitationId
    ) {
        familyInvitationService.cancelInvitation(invitationId);

        return ResponseEntity.ok(
                ApiResponse.success(200, "CANCEL_INVITATION_SUCCESS", null)
        );
    }


}