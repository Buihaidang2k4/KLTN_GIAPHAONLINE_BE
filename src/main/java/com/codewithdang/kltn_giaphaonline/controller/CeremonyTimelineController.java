package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelineReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelineUpdateReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyTimelineRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.service.ceremony.ceremony_timeline.CeremonyTimelineService;
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
@RequestMapping("${api.prefix}/ceremony-timelines")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Ceremony Timeline Management")
public class CeremonyTimelineController {
    CeremonyTimelineService timelineService;

    @PostMapping
    ResponseEntity<ApiResponse<CeremonyTimelineRes>> createTimeline(@Valid @RequestBody CeremonyTimelineReq req) {
        return ResponseEntity.ok(ApiResponse.success(201,
                "CREATE_TIMELINE_SUCCESS",
                timelineService.createCeremonyTimeline(req)));
    }

    @GetMapping("/{timelineId}")
    ResponseEntity<ApiResponse<CeremonyTimelineRes>> getById(@PathVariable Long timelineId) {
        return ResponseEntity.ok(ApiResponse.success(200,
                "GET_TIMELINE_BY_ID_SUCCESS",
                timelineService.getCeremonyTimelineById(timelineId)));
    }

    @GetMapping("/ceremony/{ceremonyId}")
    ResponseEntity<ApiResponse<PageResponse<CeremonyTimelineRes>>> getByCeremonyId(
            @PathVariable Long ceremonyId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200,
                "GET_TIMELINE_BY_CEREMONY_SUCCESS",
                timelineService.getCeremonyTimelineByCeremonyId(pageable, ceremonyId)));
    }

    @GetMapping
    ResponseEntity<ApiResponse<PageResponse<CeremonyTimelineRes>>> getAll(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200,
                "GET_ALL_TIMELINE_SUCCESS",
                timelineService.getCeremonyTimelineList(pageable)));
    }

    @PutMapping("/{timelineId}")
    ResponseEntity<ApiResponse<CeremonyTimelineRes>> updateTimeline(
            @PathVariable Long timelineId,
            @Valid @RequestBody CeremonyTimelineUpdateReq updateReq) {
        return ResponseEntity.ok(ApiResponse.success(200,
                "UPDATE_TIMELINE_SUCCESS",
                timelineService.updateCeremonyTimeline(timelineId, updateReq)));
    }

    @DeleteMapping("/{timelineId}")
    ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Long timelineId) {
        timelineService.deleteCeremonyTimelineById(timelineId);
        return ResponseEntity.ok(ApiResponse.success(200,
                "DELETE_TIMELINE_SUCCESS",
                null));
    }
}