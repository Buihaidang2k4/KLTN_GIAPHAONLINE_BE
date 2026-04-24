package com.codewithdang.kltn_giaphaonline.service.family_event;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.request.FamilyEventSearchReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyEventRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface FamilyEventService {
    // CRUD Operations
    FamilyEventRes createEvent(Long familyId, FamilyEventReq req);

    FamilyEventRes updateEvent(Long familyId, Long eventId, UpdateFamilyEventReq req);

    void deleteEvent(Long familyId, Long eventId);

    FamilyEventRes getEventById(Long eventId);

    // Query Operations
    PageResponse<FamilyEventRes> getEventsByFamily(Long familyId, Pageable pageable);

    PageResponse<FamilyEventRes> searchEvents(FamilyEventSearchReq req, Pageable pageable);

    // Scheduled Tasks
    void checkEventNotification();
}
