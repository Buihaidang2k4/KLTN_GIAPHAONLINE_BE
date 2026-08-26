package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyUpdateReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
import com.codewithdang.kltn_giaphaonline.service.ceremony.ceremony_.CeremonyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPath.Ceremony.BASE)
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Ceremony Management")
public class CeremonyController {
    CeremonyService ceremonyService;

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Ceremony.BY_ID)
    ResponseEntity<ApiResponse<CeremonyRes>> getById(@PathVariable Long ceremonyId) {
        return ResponseEntity.ok(ApiResponse.success(200,
                MessageConstantsVi.Ceremony.GET_CEREMONY_SUCCESS,
                ceremonyService.getCeremonyById(ceremonyId)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Ceremony.BY_FAMILY)
    ResponseEntity<ApiResponse<PageResponse<CeremonyRes>>> getByFamilyId(
            @PathVariable("familyId") Long familyId,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200,
                MessageConstantsVi.Ceremony.GET_CEREMONY_SUCCESS,
                ceremonyService.getCeremonyByFamilyId(familyId, keyword, pageable)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping
    ResponseEntity<ApiResponse<PageResponse<CeremonyRes>>> getCeremony(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200,
                MessageConstantsVi.Ceremony.GET_CEREMONY_LIST_SUCCESS,
                ceremonyService.getCeremonyList(pageable)));
    }

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping
    ResponseEntity<ApiResponse<CeremonyRes>> createCeremony(@RequestParam Long familyId, @Valid @RequestBody CeremonyReq ceremonyReq) {
        return ResponseEntity.ok(ApiResponse.success(201,
                MessageConstantsVi.Ceremony.CREATE_CEREMONY_SUCCESS,
                ceremonyService.createCeremony(familyId, ceremonyReq)));
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PutMapping(ApiPath.Ceremony.BY_ID)
    ResponseEntity<ApiResponse<CeremonyRes>> updateCeremony(
            @PathVariable Long ceremonyId,
            @Valid @RequestBody CeremonyUpdateReq updateReq) {
        return ResponseEntity.ok(ApiResponse.success(200,
                MessageConstantsVi.Ceremony.UPDATE_CEREMONY_SUCCESS,
                ceremonyService.updateCeremony(ceremonyId, updateReq)));
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.Ceremony.BY_ID)
    ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Long ceremonyId
    ) {
        ceremonyService.deleteCeremonyById(ceremonyId);
        return ResponseEntity.ok(ApiResponse.success(200,
                MessageConstantsVi.Ceremony.DELETE_CEREMONY_SUCCESS,
                null));
    }
}
