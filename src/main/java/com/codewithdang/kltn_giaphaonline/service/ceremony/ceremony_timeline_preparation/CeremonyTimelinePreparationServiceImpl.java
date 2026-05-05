package com.codewithdang.kltn_giaphaonline.service.ceremony.ceremony_timeline_preparation;

import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelinePreparationReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyTimelinePreparationUpdateReq;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyTimelinePreparationRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.CeremonyTimeline;
import com.codewithdang.kltn_giaphaonline.entity.CeremonyTimelinePreparation;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.CeremonyTimelinePreparationMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.CeremonyTimelinePreparationRepo;
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
public class CeremonyTimelinePreparationServiceImpl implements CeremonyTimelinePreparationService {
    CeremonyTimelinePreparationRepo ceremonyTimelinePreparationRepo;
    CeremonyTimelinePreparationMapper timelinePreparationMapper;
    PageMapper pageMapper;
    CeremonyTimelineRepo ceremonyTimelineRepo;

    @Override
    @Transactional
    public CeremonyTimelinePreparationRes createPreparation(Long timelineId, CeremonyTimelinePreparationReq req) {
        log.info("Creating preparation for timeline id: {}", timelineId);

        CeremonyTimeline ceremonyTimeline = ceremonyTimelineRepo.findById(timelineId)
                .orElseThrow(() -> new AppException(ErrorCode.CEREMONY_TIMELINE_NOT_EXISTED));

        CeremonyTimelinePreparation timelinePreparation = timelinePreparationMapper.toEntity(req);
        timelinePreparation.setTimeline(ceremonyTimeline);

        ceremonyTimelinePreparationRepo.save(timelinePreparation);
        return timelinePreparationMapper.toRes(timelinePreparation);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CeremonyTimelinePreparationRes> getPreparationList(Pageable pageable) {
        Page<CeremonyTimelinePreparation> timelinePreparationPage = ceremonyTimelinePreparationRepo.findAll(pageable);

        return pageMapper.toPageResponse(
                timelinePreparationPage,
                timelinePreparationMapper::toRes
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CeremonyTimelinePreparationRes> getPreparationByTimelineId(Pageable pageable, Long timelineId) {
        log.info("Fetching preparations for timeline id: {}", timelineId);
        Page<CeremonyTimelinePreparation> timelinePreparationPage =
                ceremonyTimelinePreparationRepo.findAllByTimeline_TimelineId(pageable, timelineId);
        return pageMapper.toPageResponse(
                timelinePreparationPage,
                timelinePreparationMapper::toRes
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CeremonyTimelinePreparationRes getPreparationById(Long preparationId) {
        CeremonyTimelinePreparation timelinePreparation = ceremonyTimelinePreparationRepo.
                findById(preparationId).orElseThrow(() -> new AppException(ErrorCode.CEREMONY_TIMELINE_PREPARATION_NOT_EXISTED));
        return timelinePreparationMapper.toRes(timelinePreparation);
    }

    @Override
    @Transactional
    public CeremonyTimelinePreparationRes updatePreparation(Long preparationId, CeremonyTimelinePreparationReq req) {
        log.info("Updating preparation id: {}", preparationId);
        CeremonyTimelinePreparation timelinePreparation = ceremonyTimelinePreparationRepo.
                findById(preparationId).orElseThrow(() -> new AppException(ErrorCode.CEREMONY_TIMELINE_PREPARATION_NOT_EXISTED));

        timelinePreparationMapper.updateEntity(req, timelinePreparation);
        ceremonyTimelinePreparationRepo.save(timelinePreparation);
        return timelinePreparationMapper.toRes(timelinePreparation);
    }

    @Override
    @Transactional
    public void deletePreparationById(Long preparationId) {
        log.info("Deleting preparation id: {}", preparationId);
        CeremonyTimelinePreparation timelinePreparation = ceremonyTimelinePreparationRepo.
                findById(preparationId).orElseThrow(() -> new AppException(ErrorCode.CEREMONY_TIMELINE_PREPARATION_NOT_EXISTED));

        ceremonyTimelinePreparationRepo.delete(timelinePreparation);
    }
}
