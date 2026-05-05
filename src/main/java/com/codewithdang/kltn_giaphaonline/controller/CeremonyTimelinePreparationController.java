package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelinePreparationReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelinePreparationUpdateReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyTimelinePreparationRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.service.ceremony.ceremony_timeline_preparation.CeremonyTimelinePreparationService;
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
@RequestMapping("${api.prefix}/ceremony-timeline-preparations")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Ceremony Timeline Preparation Management")
public class CeremonyTimelinePreparationController {
    CeremonyTimelinePreparationService preparationService;

    @PostMapping
    ResponseEntity<ApiResponse<CeremonyTimelinePreparationRes>> createPreparation(
            @RequestParam Long timelineId,
            @Valid @RequestBody CeremonyTimelinePreparationReq req) {
        return ResponseEntity.ok(ApiResponse.success(201,
                "CREATE_PREPARATION_SUCCESS",
                preparationService.createPreparation(timelineId, req)));
    }

    @GetMapping("/{preparationId}")
    ResponseEntity<ApiResponse<CeremonyTimelinePreparationRes>> getById(@PathVariable Long preparationId) {
        return ResponseEntity.ok(ApiResponse.success(200,
                "GET_PREPARATION_BY_ID_SUCCESS",
                preparationService.getPreparationById(preparationId)));
    }

    @GetMapping("/timeline/{timelineId}")
    ResponseEntity<ApiResponse<PageResponse<CeremonyTimelinePreparationRes>>> getByTimelineId(
            @PathVariable Long timelineId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200,
                "GET_PREPARATION_BY_TIMELINE_SUCCESS",
                preparationService.getPreparationByTimelineId(pageable, timelineId)));
    }

    @GetMapping
    ResponseEntity<ApiResponse<PageResponse<CeremonyTimelinePreparationRes>>> getAll(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200,
                "GET_ALL_PREPARATION_SUCCESS",
                preparationService.getPreparationList(pageable)));
    }

    @PutMapping("/{preparationId}")
    ResponseEntity<ApiResponse<CeremonyTimelinePreparationRes>> updatePreparation(
            @PathVariable Long preparationId,
            @Valid @RequestBody CeremonyTimelinePreparationReq updateReq) {
        return ResponseEntity.ok(ApiResponse.success(200,
                "UPDATE_PREPARATION_SUCCESS",
                preparationService.updatePreparation(preparationId, updateReq)));
    }

    @DeleteMapping("/{preparationId}")
    ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Long preparationId) {
        preparationService.deletePreparationById(preparationId);
        return ResponseEntity.ok(ApiResponse.success(200,
                "DELETE_PREPARATION_SUCCESS",
                null));
    }
}