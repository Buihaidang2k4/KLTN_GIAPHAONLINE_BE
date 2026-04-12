package com.codewithdang.kltn_giaphaonline.controller;


import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyUpdateReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.service.ceremony.ceremony_.CeremonyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/ceremonies")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Ceremony Management")
public class CeremonyController {
    CeremonyService ceremonyService;

    @GetMapping("/{ceremonyId}")
    ResponseEntity<ApiResponse<CeremonyRes>> getById(@PathVariable Long ceremonyId) {
        return ResponseEntity.ok(ApiResponse.success(200,
                "Lấy thông tin nghi lễ thành công",
                ceremonyService.getCeremonyById(ceremonyId)));
    }

    @GetMapping("/family/{familyId}")
    ResponseEntity<ApiResponse<PageResponse<CeremonyRes>>> getByFamilyId(
            @PathVariable("familyId") Long familyId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200,
                "Lấy thông tin nghi lễ thành công",
                ceremonyService.getCeremonyByFamilyId(pageable, familyId)));
    }


    @GetMapping
    ResponseEntity<ApiResponse<PageResponse<CeremonyRes>>> getCeremony(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200,
                "Tải danh sách nghi lễ thành công",
                ceremonyService.getCeremonyList(pageable)));
    }

    @PostMapping
    ResponseEntity<ApiResponse<CeremonyRes>> createCeremony(@Valid @RequestBody CeremonyReq ceremonyReq) {
        return ResponseEntity.ok(ApiResponse.success(201,
                "Tạo nghi lễ mới thành công",
                ceremonyService.createCeremony(ceremonyReq)));
    }

    @PutMapping("/{ceremonyId}")
    ResponseEntity<ApiResponse<CeremonyRes>> updateCeremony(
            @PathVariable Long ceremonyId,
            @Valid @RequestBody CeremonyUpdateReq updateReq) {
        return ResponseEntity.ok(ApiResponse.success(200,
                "Cập nhật nghi lễ thành công",
                ceremonyService.updateCeremony(ceremonyId, updateReq)));
    }

    @DeleteMapping("/{ceremonyId}")
    ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Long ceremonyId) {
        ceremonyService.deleteCeremonyById(ceremonyId);
        return ResponseEntity.ok(ApiResponse.success(200,
                "Đã xóa nghi lễ thành công",
                null));
    }
}
