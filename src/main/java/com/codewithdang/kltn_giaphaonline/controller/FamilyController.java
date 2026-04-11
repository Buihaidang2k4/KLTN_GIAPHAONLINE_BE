package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.service.family.FamilyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/families")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FamilyController {
    FamilyService familyService;

    @PostMapping
    public ResponseEntity<ApiResponse<FamilyRes>> createFamily(@RequestBody FamilyReq familyRequest) {
        return ResponseEntity.ok(ApiResponse.success(200, "CREATE_FAMILY_SUCCESS", familyService.createFamily(familyRequest)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FamilyRes>>> getAllFamilies(Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, "CREATE_FAMILY_SUCCESS", familyService.getFamilies(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FamilyRes> getFamilyById(@PathVariable Long id) {
        FamilyRes familyResponse = familyService.getFamilyById(id);
        return new ResponseEntity<>(familyResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFamily(@PathVariable Long id) {
        familyService.deleteFamilyById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT).ok(ApiResponse.success(200, "DELETE_FAMILY_SUCCESS", null));
    }
}
