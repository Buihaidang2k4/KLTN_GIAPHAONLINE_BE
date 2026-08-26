package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyMemberRoleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyMemberRes;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
import com.codewithdang.kltn_giaphaonline.service.family_member.FamilyMemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPath.FamilyMember.BASE)
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Family Member Management")
public class FamilyMemberController {

    FamilyMemberService familyMemberService;

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.FamilyMember.BY_FAMILY)
    public ResponseEntity<ApiResponse<List<FamilyMemberRes>>> getFamilyMemberByFamilyId(@PathVariable Long familyId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyMember.GET_FAMILY_MEMBER_SUCCESS,
                familyMemberService.getFamilyMemberByFamilyId(familyId)));
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PatchMapping(ApiPath.FamilyMember.UPDATE_ROLE)
    public ResponseEntity<ApiResponse<Void>> updateMemberRole(
            @PathVariable Long familyId,
            @PathVariable Long targetAccountId,
            @RequestBody @Valid UpdateFamilyMemberRoleReq request
    ) {
        familyMemberService.updateMemberRole(familyId, targetAccountId, request);
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.FamilyMember.UPDATE_MEMBER_ROLE_SUCCESS, null)
        );
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.FamilyMember.REMOVE_MEMBER)
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @PathVariable Long familyId,
            @PathVariable Long targetAccountId
    ) {
        familyMemberService.removeMember(familyId, targetAccountId);
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyMember.REMOVE_MEMBER_SUCCESS, null));
    }
}