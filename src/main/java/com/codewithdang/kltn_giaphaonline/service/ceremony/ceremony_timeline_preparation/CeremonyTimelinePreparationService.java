package com.codewithdang.kltn_giaphaonline.service.ceremony.ceremony_timeline_preparation;

import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelinePreparationReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelinePreparationUpdateReq;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyTimelinePreparationRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface CeremonyTimelinePreparationService {
    CeremonyTimelinePreparationRes createPreparation(CeremonyTimelinePreparationReq req);

    PageResponse<CeremonyTimelinePreparationRes> getPreparationList(Pageable pageable);

    PageResponse<CeremonyTimelinePreparationRes> getPreparationByTimelineId(Pageable pageable, Long timelineId);

    CeremonyTimelinePreparationRes getPreparationById(Long preparationId);

    CeremonyTimelinePreparationRes updatePreparation(Long preparationId, CeremonyTimelinePreparationUpdateReq req);

    void deletePreparationById(Long preparationId);
}
