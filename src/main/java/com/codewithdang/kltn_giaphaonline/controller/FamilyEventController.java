package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.FamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyEventRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
import com.codewithdang.kltn_giaphaonline.enums.SearchEventOptionEnum;
import com.codewithdang.kltn_giaphaonline.service.family_event.FamilyEventService;
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
@RequestMapping(ApiPath.FamilyEvent.BASE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Family Event Management")
public class FamilyEventController {

    FamilyEventService familyEventService;

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping(ApiPath.FamilyEvent.BY_FAMILY)
    public ResponseEntity<ApiResponse<FamilyEventRes>> createEvent(
            @PathVariable Long familyId,
            @Valid @RequestBody FamilyEventReq request) {
        return ResponseEntity.ok(ApiResponse.success(201, MessageConstantsVi.FamilyEvent.CREATE_EVENT_SUCCESS,
                familyEventService.createEvent(familyId, request)));
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PutMapping(ApiPath.FamilyEvent.FAMILY_EVENT_BY_ID)
    public ResponseEntity<ApiResponse<FamilyEventRes>> updateEvent(
            @PathVariable Long familyId,
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateFamilyEventReq request) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyEvent.UPDATE_EVENT_SUCCESS,
                familyEventService.updateEvent(familyId, eventId, request)));
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.FamilyEvent.FAMILY_EVENT_BY_ID)
    public ResponseEntity<ApiResponse<Void>> deleteEvent(
            @PathVariable Long familyId,
            @PathVariable Long eventId) {
        familyEventService.deleteEvent(familyId, eventId);
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyEvent.DELETE_EVENT_SUCCESS, null));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.FamilyEvent.BY_FAMILY)
    public ResponseEntity<ApiResponse<PageResponse<FamilyEventRes>>> getEventsByFamily(
            @PathVariable Long familyId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "ALL") SearchEventOptionEnum option,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyEvent.GET_EVENTS_BY_FAMILY_SUCCESS,
                familyEventService.getEventsByFamily(familyId, keyword, option, pageable)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.FamilyEvent.BY_ID)
    public ResponseEntity<ApiResponse<FamilyEventRes>> getEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyEvent.GET_EVENT_SUCCESS,
                familyEventService.getEventById(eventId)));
    }
}