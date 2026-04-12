package com.codewithdang.kltn_giaphaonline.service.ceremony.ceremony_timeline;

import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelineReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelineUpdateReq;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyTimelineRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface CeremonyTimelineService {
    CeremonyTimelineRes createCeremonyTimeline(CeremonyTimelineReq req);

    PageResponse<CeremonyTimelineRes> getCeremonyTimelineList(Pageable pageable);

    PageResponse<CeremonyTimelineRes> getCeremonyTimelineByCeremonyId(Pageable pageable, Long ceremonyId);

    CeremonyTimelineRes getCeremonyTimelineById(Long timelineId);

    CeremonyTimelineRes updateCeremonyTimeline(Long timelineId, CeremonyTimelineUpdateReq req);

    void deleteCeremonyTimelineById(Long timelineId);
}
