package com.codewithdang.kltn_giaphaonline.service.ceremony.ceremony_timeline_preparation;

import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelinePreparationReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelinePreparationUpdateReq;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyTimelinePreparationRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CeremonyTimelinePreparationServiceImpl implements CeremonyTimelinePreparationService {
    @Override
    public CeremonyTimelinePreparationRes createPreparation(CeremonyTimelinePreparationReq req) {
        return null;
    }

    @Override
    public PageResponse<CeremonyTimelinePreparationRes> getPreparationList(Pageable pageable) {
        return null;
    }

    @Override
    public PageResponse<CeremonyTimelinePreparationRes> getPreparationByTimelineId(Pageable pageable, Long timelineId) {
        return null;
    }

    @Override
    public CeremonyTimelinePreparationRes getPreparationById(Long preparationId) {
        return null;
    }

    @Override
    public CeremonyTimelinePreparationRes updatePreparation(Long preparationId, CeremonyTimelinePreparationUpdateReq req) {
        return null;
    }

    @Override
    public void deletePreparationById(Long preparationId) {

    }
}
