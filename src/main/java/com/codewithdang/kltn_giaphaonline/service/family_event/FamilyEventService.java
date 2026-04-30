package com.codewithdang.kltn_giaphaonline.service.family_event;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyEventRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.SearchEventOptionEnum;
import org.springframework.data.domain.Pageable;

public interface FamilyEventService {
    // CRUD Operations
    FamilyEventRes createEvent(Long familyId, FamilyEventReq req);

    FamilyEventRes updateEvent(Long familyId, Long eventId, UpdateFamilyEventReq req);

    void deleteEvent(Long familyId, Long eventId);

    FamilyEventRes getEventById(Long eventId);

    PageResponse<FamilyEventRes> getEventsByFamily(Long familyId, String keyword, SearchEventOptionEnum optionEnum, Pageable pageable);

    void refreshNextOccurrenceDates();
}
