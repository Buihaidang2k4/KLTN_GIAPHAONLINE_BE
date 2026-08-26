package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.FamilyReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
import com.codewithdang.kltn_giaphaonline.service.family.FamilyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.Family.BASE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FamilyController {
    FamilyService familyService;

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping
    public ResponseEntity<ApiResponse<FamilyRes>> createFamily(@RequestBody FamilyReq familyRequest) {
        return ResponseEntity.ok(ApiResponse.success(201, MessageConstantsVi.Family.CREATE_FAMILY_SUCCESS, familyService.createFamily(familyRequest)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FamilyRes>>> getAllFamilies(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Family.GET_FAMILY_SUCCESS, familyService.getFamilies(pageable)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Family.CURRENT_ACCOUNT)
    public ResponseEntity<ApiResponse<PageResponse<FamilyRes>>> getAllFamiliesCurrentAccount(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Family.GET_FAMILY_SUCCESS, familyService.getFamiliesByCurrentAccount(pageable)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Family.BY_ID)
    public ResponseEntity<FamilyRes> getFamilyById(@PathVariable Long id) {
        FamilyRes familyResponse = familyService.getFamilyById(id);
        return new ResponseEntity<>(familyResponse, HttpStatus.OK);
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.Family.BY_ID)
    public ResponseEntity<ApiResponse<Void>> deleteFamily(@PathVariable Long id) {
        familyService.deleteFamilyById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT).ok(ApiResponse.success(200, MessageConstantsVi.Family.DELETE_FAMILY_SUCCESS, null));
    }
}
