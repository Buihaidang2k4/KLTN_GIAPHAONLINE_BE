package com.codewithdang.kltn_giaphaonline.service.ceremony.ceremony_timeline;

import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelineReq;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyTimelineRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Ceremony;
import com.codewithdang.kltn_giaphaonline.entity.CeremonyTimeline;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.CeremonyTimelineMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.CeremonyRepo;
import com.codewithdang.kltn_giaphaonline.repo.CeremonyTimelineRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CeremonyTimelineServiceImpl implements CeremonyTimelineService {
    CeremonyTimelineRepo ceremonyTimelineRepo;
    CeremonyTimelineMapper timelineMapper;
    CeremonyRepo ceremonyRepo;
    PageMapper pageMapper;

    @Override
    @Transactional
    public CeremonyTimelineRes createCeremonyTimeline(Long ceremonyId, CeremonyTimelineReq req) {
        Ceremony ceremony = ceremonyRepo.findById(ceremonyId).
                orElseThrow(() -> new AppException(ErrorCode.CEREMONY_NOT_EXISTED));

        // tinh step lon nhat
        Integer maxOrder = ceremonyTimelineRepo.findMaxStepOrderByCeremonyId(ceremony.getCeremonyId());
        int nextOrder = (maxOrder == null) ? 1 : maxOrder + 1;

        // gan gia tri
        CeremonyTimeline timeline = timelineMapper.toEntity(req);
        timeline.setCeremony(ceremony);
        timeline.setStepOrder(nextOrder);

        log.info("Creating timeline step {} for ceremony {}", nextOrder, ceremonyId);
        ceremonyTimelineRepo.save(timeline);

        return timelineMapper.toRes(timeline);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CeremonyTimelineRes> getCeremonyTimelineList(Pageable pageable) {
        Page<CeremonyTimeline> timelinePage = ceremonyTimelineRepo.findAll(pageable);
        return pageMapper.toPageResponse(
                timelinePage,
                timelineMapper::toRes
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CeremonyTimelineRes> getCeremonyTimelineByCeremonyId(Pageable pageable, Long ceremonyId) {
        Page<CeremonyTimeline> timelinePage = ceremonyTimelineRepo.findAllByCeremony_CeremonyId(pageable, ceremonyId);
        return pageMapper.toPageResponse(timelinePage, timelineMapper::toRes);
    }

    @Override
    @Transactional(readOnly = true)
    public CeremonyTimelineRes getCeremonyTimelineById(Long timelineId) {
        CeremonyTimeline ceremonyTimeline = ceremonyTimelineRepo.findById(timelineId).orElseThrow(
                () -> new AppException(ErrorCode.CEREMONY_TIMELINE_NOT_EXISTED)
        );
        return timelineMapper.toRes(ceremonyTimeline);
    }

    @Override
    @Transactional
    public CeremonyTimelineRes updateCeremonyTimeline(Long timelineId, CeremonyTimelineReq req) {
        CeremonyTimeline ceremonyTimeline = ceremonyTimelineRepo.findById(timelineId)
                .orElseThrow(() -> new AppException(ErrorCode.CEREMONY_TIMELINE_NOT_EXISTED));

        timelineMapper.updateEntity(req, ceremonyTimeline);
        ceremonyTimelineRepo.save(ceremonyTimeline);
        return timelineMapper.toRes(ceremonyTimeline);
    }

    @Override
    @Transactional
    public void deleteCeremonyTimelineById(Long timelineId) {
        // 1. find step need remove
        CeremonyTimeline ceremonyTimeline = ceremonyTimelineRepo.findById(timelineId).orElseThrow(() -> new AppException(ErrorCode.CEREMONY_TIMELINE_NOT_EXISTED));

        Long ceremonyId = ceremonyTimeline.getCeremony().getCeremonyId();
        int deletedOrder = ceremonyTimeline.getStepOrder();

        // 2. delete step
        ceremonyTimelineRepo.delete(ceremonyTimeline);

        // shift order down
        ceremonyTimelineRepo.shiftOrdersDown(ceremonyId, deletedOrder);
        log.info("Deleted timeline step {} and shifted subsequent steps for ceremony {}", deletedOrder, ceremonyId);
    }
}
