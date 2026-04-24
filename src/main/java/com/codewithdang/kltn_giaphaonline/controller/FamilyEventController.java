package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.request.FamilyEventSearchReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyEventRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.service.family_event.FamilyEventService;

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
    public ResponseEntity<ApiResponse<FamilyEventRes>> createEvent(@RequestParam("familyId") Long familyId, @RequestBody FamilyEventReq request) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        201,
                        "CREATE_EVENT_SUCCESS",
                        familyEventService.createEvent(familyId, request)
                )
        );
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<ApiResponse<FamilyEventRes>> updateEvent(
            @PathVariable Long eventId,
            @RequestParam("familyId") Long familyId,
            @RequestBody UpdateFamilyEventReq request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "UPDATE_EVENT_SUCCESS",
                        familyEventService.updateEvent(familyId, eventId, request)
                )
        );
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@RequestParam Long familyId, @PathVariable Long eventId) {
        familyEventService.deleteEvent(familyId, eventId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "DELETE_EVENT_SUCCESS",
                        null
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

    @GetMapping("/family/{familyId}")
    public ResponseEntity<ApiResponse<PageResponse<FamilyEventRes>>> getEventsByFamily(
            @PathVariable Long familyId,
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "GET_EVENTS_BY_FAMILY_SUCCESS",
                        familyEventService.getEventsByFamily(familyId, pageable)
                )
        );
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<FamilyEventRes>>> searchEvents(
            @RequestBody FamilyEventSearchReq searchReq,
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "SEARCH_EVENTS_SUCCESS",
                        familyEventService.searchEvents(searchReq, pageable)
                )
        );
    }


}