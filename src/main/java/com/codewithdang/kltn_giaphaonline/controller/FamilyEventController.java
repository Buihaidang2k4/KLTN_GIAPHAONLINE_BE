package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyEventRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.SearchEventOptionEnum;
import com.codewithdang.kltn_giaphaonline.service.family_event.FamilyEventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.prefix}/family-events")
@RequiredArgsConstructor
public class FamilyEventController {

    private final FamilyEventService familyEventService;

    @PostMapping
    public ResponseEntity<ApiResponse<FamilyEventRes>> createEvent(@RequestParam("familyId") Long familyId, @Valid @RequestBody FamilyEventReq request) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        201,
                        "CREATE_EVENT_SUCCESS",
                        familyEventService.createEvent(familyId, request)
                )
        );
    }

    @PutMapping("/family/{familyId}/event/{eventId}")
    public ResponseEntity<ApiResponse<FamilyEventRes>> updateEvent(
            @PathVariable Long eventId,
            @PathVariable("familyId") Long familyId,
            @Valid @RequestBody UpdateFamilyEventReq request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "UPDATE_EVENT_SUCCESS",
                        familyEventService.updateEvent(familyId, eventId, request)
                )
        );
    }

    @DeleteMapping("/family/{familyId}/event/{eventId}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long familyId, @Valid @PathVariable Long eventId) {
        familyEventService.deleteEvent(familyId, eventId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "DELETE_EVENT_SUCCESS",
                        null
                )
        );
    }

    @GetMapping("/family/{familyId}")
    public ResponseEntity<ApiResponse<PageResponse<FamilyEventRes>>> getEventsByFamily(
            @PathVariable Long familyId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "ALL") SearchEventOptionEnum option,
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "GET_EVENTS_BY_FAMILY_SUCCESS",
                        familyEventService.getEventsByFamily(familyId, keyword, option, pageable)
                )
        );
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<FamilyEventRes>> getEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "GET_EVENT_SUCCESS",
                        familyEventService.getEventById(eventId)
                )
        );
    }


}