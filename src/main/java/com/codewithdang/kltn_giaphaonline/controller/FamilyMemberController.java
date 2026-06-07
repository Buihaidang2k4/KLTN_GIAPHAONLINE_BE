package com.codewithdang.kltn_giaphaonline.controller;


import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyMemberRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyMemberRes;
import com.codewithdang.kltn_giaphaonline.service.family_member.FamilyMemberService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/families-members")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FamilyMemberController {

    FamilyMemberService familyMemberService;

    @GetMapping("/{familyId}")
    public ResponseEntity<ApiResponse<List<FamilyMemberRes>>> getFamilyMemberByFamilyId(@PathVariable Long familyId) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_FAMILY_MEMBER_SUCCESS", familyMemberService.getFamilyMemberByFamilyId(familyId)));
    }

    @PatchMapping("/{familyId}/members/{targetAccountId}/role")
    public ResponseEntity<ApiResponse<Void>> updateMemberRole(
            @PathVariable Long familyId,
            @PathVariable Long targetAccountId,
            @RequestBody @Valid UpdateFamilyMemberRoleReq request
    ) {
        familyMemberService.updateMemberRole(familyId, targetAccountId, request);

        return ResponseEntity.ok(
                ApiResponse.success(200, "UPDATE_MEMBER_ROLE_SUCCESS", null)
        );
    }

    @DeleteMapping("/{familyId}/accounts/{targetAccountId}")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @PathVariable Long familyId,
            @PathVariable Long targetAccountId
    ) {
        familyMemberService.removeMember(familyId, targetAccountId);
        return ResponseEntity.ok(ApiResponse.success(200, "REMOVE_MEMBER_SUCCESS", null));
    }
}