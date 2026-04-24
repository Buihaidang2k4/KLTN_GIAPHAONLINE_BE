package com.codewithdang.kltn_giaphaonline.service.family_event;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.request.FamilyEventSearchReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyEventRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.FamilyEvent;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.FamilyEventMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.FamilyEventRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyRepo;
import com.codewithdang.kltn_giaphaonline.service.account.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FamilyEventServiceImpl implements FamilyEventService {
    FamilyEventRepo familyEventRepo;
    FamilyEventMapper familyEventMapper;
    PageMapper pageMapper;
    FamilyRepo familyRepo;
    AccountService accountService;

    @Override
    @Transactional
    public FamilyEventRes createEvent(Long familyId, FamilyEventReq req) {
        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));

        Account account = accountService.getCurrentAccount();
        FamilyEvent event = familyEventMapper.toEntity(req);

        event.setFamily(family);
        event.setCreatedByAccount(account);
        event = familyEventRepo.save(event);
        return familyEventMapper.toDto(event);
    }

    @Override
    @Transactional
    public FamilyEventRes updateEvent(Long familyId, Long eventId, UpdateFamilyEventReq req) {
        FamilyEvent event = familyEventRepo.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_EVENT_NOT_EXISTED));

        if (!event.getFamily().getFamilyId().equals(familyId)) {
            throw new AppException(ErrorCode.FAMILY_EVENT_NOT_IN_FAMILY);
        }
        familyEventMapper.updateEvent(req, event);
        event = familyEventRepo.save(event);
        return familyEventMapper.toDto(event);
    }

    @Override
    @Transactional
    public void deleteEvent(Long familyId, Long eventId) {
        FamilyEvent event = familyEventRepo
                .findByFamily_FamilyIdAndFamilyEventId(familyId, eventId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_EVENT_IS_EXISTED));

        familyEventRepo.delete(event);
    }

    @Override
    @Transactional(readOnly = true)
    public FamilyEventRes getEventById(Long eventId) {
        FamilyEvent event = familyEventRepo.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_EVENT_NOT_EXISTED));
        return familyEventMapper.toDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FamilyEventRes> getEventsByFamily(Long familyId, Pageable pageable) {
        Page<FamilyEvent> events = familyEventRepo.findAllByFamily_FamilyId(familyId, pageable);
        return pageMapper.toPageResponse(events, familyEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FamilyEventRes> searchEvents(FamilyEventSearchReq req, Pageable pageable) {
        log.info("Searching family events with criteria: {}", req);
        Page<FamilyEvent> familyEventPage = familyEventRepo.searchEvents(
                req.getFamilyId(),
                req.getKeyword(),
                req.getStartDate(),
                req.getEndDate(),
                req.getCalendarType(),
                req.getReminderType(),
                pageable
        );

        return pageMapper.toPageResponse(familyEventPage,
                familyEventMapper::toDto
        );
    }

    @Override
    @Scheduled(fixedRate = 60000)
    public void checkEventNotification() {

    }

}
