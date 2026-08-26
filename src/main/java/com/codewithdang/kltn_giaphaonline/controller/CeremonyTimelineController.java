package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelineReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyTimelineRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
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
@RequestMapping(ApiPath.CeremonyTimeline.BASE)
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Ceremony Timeline Management")
public class CeremonyTimelineController {
    CeremonyTimelineService timelineService;

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping
    public ResponseEntity<ApiResponse<CeremonyTimelineRes>> create(
            @RequestParam Long ceremonyId,
            @Valid @RequestBody CeremonyTimelineReq req) {
        return ResponseEntity.status(201).body(ApiResponse.success(201, MessageConstantsVi.CeremonyTimeline.CREATE_TIMELINE_SUCCESS,
                timelineService.createCeremonyTimeline(ceremonyId, req)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.CeremonyTimeline.BY_ID)
    public ResponseEntity<ApiResponse<CeremonyTimelineRes>> getById(@PathVariable Long timelineId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.CeremonyTimeline.GET_TIMELINE_BY_ID_SUCCESS,
                timelineService.getCeremonyTimelineById(timelineId)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.CeremonyTimeline.BY_CEREMONY)
    public ResponseEntity<ApiResponse<PageResponse<CeremonyTimelineRes>>> getByCeremonyId(
            @PathVariable Long ceremonyId,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(sort = "stepOrder", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.CeremonyTimeline.GET_TIMELINE_BY_CEREMONY_SUCCESS,
                timelineService.getCeremonyTimelineByCeremonyId(pageable, ceremonyId)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CeremonyTimelineRes>>> getAll(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.CeremonyTimeline.GET_ALL_TIMELINE_SUCCESS,
                timelineService.getCeremonyTimelineList(pageable)));
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PutMapping(ApiPath.CeremonyTimeline.BY_ID)
    public ResponseEntity<ApiResponse<CeremonyTimelineRes>> update(
            @PathVariable Long timelineId,
            @Valid @RequestBody CeremonyTimelineReq req) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.CeremonyTimeline.UPDATE_TIMELINE_SUCCESS,
                timelineService.updateCeremonyTimeline(timelineId, req)));
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.CeremonyTimeline.BY_ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long timelineId) {
        timelineService.deleteCeremonyTimelineById(timelineId);
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.CeremonyTimeline.DELETE_TIMELINE_SUCCESS,
                null));
    }
}