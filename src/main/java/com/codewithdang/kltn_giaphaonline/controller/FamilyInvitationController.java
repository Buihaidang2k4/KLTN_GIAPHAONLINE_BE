package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.CreateFamilyInvitationReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.InviteInvitationMemberRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
import com.codewithdang.kltn_giaphaonline.service.family_invitation.FamilyInvitationService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping(ApiPath.FamilyInvitation.BASE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Family Invitation Management")
public class FamilyInvitationController {

    FamilyInvitationService familyInvitationService;

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.FamilyInvitation.SENT)
    public ResponseEntity<ApiResponse<PageResponse<InviteInvitationMemberRes>>> getSentInvitations(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable

    ) {
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.FamilyInvitation.GET_MY_INVITATION_SENT_SUCCESS,
                        familyInvitationService.getMyInvitationsSent(pageable)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.FamilyInvitation.RECEIVED)
    public ResponseEntity<ApiResponse<PageResponse<InviteInvitationMemberRes>>> receivedInvitation(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.FamilyInvitation.GET_MY_INVITATION_RECEIVED_SUCCESS,
                        familyInvitationService.getMyInvitationsReceived(pageable)));
    }

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping(ApiPath.FamilyInvitation.INVITE)
    public ResponseEntity<ApiResponse<InviteInvitationMemberRes>> inviteMember(
            @PathVariable Long familyId,
            @RequestBody @Valid CreateFamilyInvitationReq request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        MessageConstantsVi.FamilyInvitation.INVITE_MEMBER_SUCCESS,
                        familyInvitationService.inviteMember(familyId, request)
                )
        );
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PostMapping(ApiPath.FamilyInvitation.ACCEPT)
    public ResponseEntity<ApiResponse<Void>> acceptInvitation(
            @PathVariable String token
    ) {
        familyInvitationService.acceptInvitation(token);

        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.FamilyInvitation.ACCEPT_INVITATION_SUCCESS, null)
        );
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PostMapping(ApiPath.FamilyInvitation.REJECT)
    public ResponseEntity<ApiResponse<Void>> rejectInvitation(
            @PathVariable String token
    ) {
        familyInvitationService.rejectInvitation(token);

        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.FamilyInvitation.REJECT_INVITATION_SUCCESS, null)
        );
    }

    @OperatorAction(CommonEnums.Operator.CANCEL)
    @PostMapping(ApiPath.FamilyInvitation.CANCEL)
    public ResponseEntity<ApiResponse<Void>> cancelInvitation(
            @PathVariable Long invitationId
    ) {
        familyInvitationService.cancelInvitation(invitationId);

        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.FamilyInvitation.CANCEL_INVITATION_SUCCESS, null)
        );
    }
}