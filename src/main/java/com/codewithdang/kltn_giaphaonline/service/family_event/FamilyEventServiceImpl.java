package com.codewithdang.kltn_giaphaonline.service.family_event;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateAuditLogReq;
import com.codewithdang.kltn_giaphaonline.dto.request.FamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyEventRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.FamilyEvent;
import com.codewithdang.kltn_giaphaonline.enums.AuditAction;
import com.codewithdang.kltn_giaphaonline.enums.AuditEntityType;
import com.codewithdang.kltn_giaphaonline.enums.CalendarType;
import com.codewithdang.kltn_giaphaonline.enums.RepeatType;
import com.codewithdang.kltn_giaphaonline.enums.SearchEventOptionEnum;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.FamilyEventMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.FamilyEventRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyRepo;
import com.codewithdang.kltn_giaphaonline.service.account.AccountService;
import com.codewithdang.kltn_giaphaonline.service.audit_log.AuditLogService;
import com.codewithdang.kltn_giaphaonline.service.notification.NotificationService;
import com.codewithdang.kltn_giaphaonline.utils.LunarSolarConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    NotificationService notificationService;
    AuditLogService auditLogService;

    @Override
    @Transactional
    public FamilyEventRes createEvent(Long familyId, FamilyEventReq req) {
        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));

        Account account = accountService.getCurrentAccount();

        FamilyEvent event = familyEventMapper.toEntity(req);

        if (req.getEventName() != null) {
            event.setEventName(req.getEventName().trim());
        }

        event.setCreatedByAccount(account);
        event.setFamily(family);

        normalizeEvent(event);

        LocalDate nextDate = computeNextOccurrence(event, LocalDate.now());
        event.setNextOccurrenceDate(nextDate);

        FamilyEvent savedEvent = familyEventRepo.save(event);

        auditLogService.log(CreateAuditLogReq.builder()
                .familyId(familyId)
                .actorAccountId(account.getAccountId())
                .auditAction(AuditAction.EVENT_CREATE.getLabel())
                .entityType(AuditEntityType.FAMILY.name())
                .newData(buildEventDataMap(savedEvent))
                .entityId(savedEvent.getFamilyEventId().toString())
                .build());

        return familyEventMapper.toDto(savedEvent);
    }

    @Override
    @Transactional
    public FamilyEventRes updateEvent(Long familyId, Long eventId, UpdateFamilyEventReq req) {
        FamilyEvent event = familyEventRepo.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_EVENT_NOT_EXISTED));

        if (!event.getFamily().getFamilyId().equals(familyId)) {
            throw new AppException(ErrorCode.FAMILY_EVENT_NOT_IN_FAMILY);
        }

        Map<String, Object> oldData = buildEventDataMap(event);

        familyEventMapper.updateEvent(req, event);

        if (event.getEventName() != null) {
            event.setEventName(event.getEventName().trim());
        }

        normalizeEvent(event);

        LocalDate nextDate = computeNextOccurrence(event, LocalDate.now());
        event.setNextOccurrenceDate(nextDate);

        FamilyEvent savedEvent = familyEventRepo.save(event);

        Account account = accountService.getCurrentAccount();
        auditLogService.log(CreateAuditLogReq.builder()
                .familyId(familyId)
                .actorAccountId(account.getAccountId())
                .auditAction(AuditAction.EVENT_UPDATE.getLabel())
                .entityType(AuditEntityType.FAMILY.name())
                .oldData(oldData)
                .newData(buildEventDataMap(savedEvent))
                .entityId(savedEvent.getFamilyEventId().toString())
                .build());

        return familyEventMapper.toDto(savedEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(Long familyId, Long eventId) {
        FamilyEvent event = familyEventRepo
                .findByFamily_FamilyIdAndFamilyEventId(familyId, eventId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_EVENT_NOT_EXISTED));

        Account account = accountService.getCurrentAccount();
        Map<String, Object> oldData = buildEventDataMap(event);

        familyEventRepo.delete(event);

        auditLogService.log(CreateAuditLogReq.builder()
                .familyId(familyId)
                .actorAccountId(account.getAccountId())
                .auditAction(AuditAction.EVENT_DELETE.getLabel())
                .entityType(AuditEntityType.FAMILY.name())
                .oldData(oldData)
                .entityId(eventId.toString())
                .build());
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
    public PageResponse<FamilyEventRes> getEventsByFamily(
            Long familyId,
            String keyword,
            SearchEventOptionEnum optionEnum,
            Pageable pageable
    ) {
        SearchEventOptionEnum option = optionEnum == null
                ? SearchEventOptionEnum.ALL
                : optionEnum;

        Page<FamilyEvent> events;

        switch (option) {
            case UPCOMING -> {
                LocalDate today = LocalDate.now();
                LocalDate next30Days = today.plusDays(30);
                events = familyEventRepo.findUpcomingEvents(
                        familyId, keyword, today, next30Days, pageable);
            }
            default -> events = familyEventRepo.findAllByFamily_FamilyIdAndKeyword(
                    familyId, keyword, pageable);
        }

        return pageMapper.toPageResponse(events, familyEventMapper::toDto);
    }

    /**
     * Chạy mỗi ngày lúc 00:00 để cập nhật nextOccurrenceDate cho sự kiện YEARLY đã qua.
     * Gửi thông báo email cho các thành viên gia đình sự kiện sắp tới 30 ngày
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void refreshNextOccurrenceDates() {
        LocalDate today = LocalDate.now();

        List<FamilyEvent> dueEvents =
                familyEventRepo.findAllByNextOccurrenceDateLessThanEqual(today);

        if (dueEvents == null || dueEvents.isEmpty()) {
            return;
        }

        List<FamilyEvent> updatedEvents = new ArrayList<>();

        for (FamilyEvent event : dueEvents) {
            try {
                if (event.getRepeatType() != RepeatType.YEARLY) {
                    // event một lần đã qua → set null để không load lại mỗi ngày
                    event.setNextOccurrenceDate(null);
                    updatedEvents.add(event);
                    continue;
                }

                LocalDate nextDate = computeNextOccurrence(event, today.plusDays(1));

                if (nextDate != null) {
                    event.setNextOccurrenceDate(nextDate);
                    updatedEvents.add(event);
                }
            } catch (Exception ex) {
                log.error("Failed to refresh event {}: {}", event.getFamilyEventId(), ex.getMessage(), ex);
            }
        }

        if (!updatedEvents.isEmpty()) {
            familyEventRepo.saveAll(updatedEvents);
        }
    }

    /**
     * Chuẩn hóa event:
     * - repeatType null => NONE
     * - calendarType null => SOLAR
     * - YEARLY => year = null
     */
    private Map<String, Object> buildEventDataMap(FamilyEvent event) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("Tên sự kiện", String.valueOf(event.getEventName()));
        data.put("Ngày", event.getDay() + "/" + event.getMonth() + (event.getYear() != null ? "/" + event.getYear() : ""));
        data.put("Lịch", event.getCalendarType() != null ? event.getCalendarType().name() : null);
        data.put("Lặp lại", event.getRepeatType() != null ? event.getRepeatType().name() : null);
        if (event.getLocation() != null) data.put("Vị trí", event.getLocation());
        if (event.getNote() != null) data.put("Ghi chú", event.getNote());
        return data;
    }

    private void normalizeEvent(FamilyEvent event) {
        if (event.getRepeatType() == null) {
            event.setRepeatType(RepeatType.NONE);
        }

        if (event.getCalendarType() == null) {
            event.setCalendarType(CalendarType.SOLAR);
        }

        if (event.getRepeatType() == RepeatType.YEARLY) {
            event.setYear(null);
        }
    }

    /**
     * Tính ngày diễn ra tiếp theo.
     * NONE:
     * - Cần có day/month/year.
     * - Nếu ngày đã qua => null.
     * YEARLY:
     * - Chỉ cần day/month.
     * - Nếu năm nay chưa tới => trả năm nay.
     * - Nếu năm nay qua rồi => trả năm sau.
     * LUNAR:
     * - Tạm thời chưa xử lý convert âm -> dương.
     */
    private LocalDate computeNextOccurrence(FamilyEvent event, LocalDate fromDate) {
        if (event == null || fromDate == null) {
            return null;
        }

        Integer day = event.getDay();
        Integer month = event.getMonth();
        Integer year = event.getYear();

        if (day == null || month == null) {
            return null;
        }

        if (event.getCalendarType() == CalendarType.LUNAR) {
            return computeLunarYearlyOccurrence(day, month, fromDate, event.getRepeatType());
        }

        RepeatType repeatType = event.getRepeatType() == null
                ? RepeatType.NONE
                : event.getRepeatType();

        try {
            if (repeatType == RepeatType.YEARLY) {
                return computeYearlyOccurrence(day, month, fromDate);
            }

            return computeOneTimeOccurrence(day, month, year, fromDate);

        } catch (DateTimeException ex) {
            log.warn(
                    "Invalid event date. eventId={}, day={}, month={}, year={}",
                    event.getFamilyEventId(),
                    day,
                    month,
                    year
            );
            return null;
        }
    }

    private LocalDate computeOneTimeOccurrence(
            Integer day,
            Integer month,
            Integer year,
            LocalDate fromDate
    ) {
        if (year == null) {
            return null;
        }

        LocalDate candidate = LocalDate.of(year, month, day);

        return candidate.isBefore(fromDate) ? null : candidate;
    }

    private LocalDate computeYearlyOccurrence(
            Integer day,
            Integer month,
            LocalDate fromDate
    ) {
        LocalDate candidate = LocalDate.of(fromDate.getYear(), month, day);

        if (!candidate.isBefore(fromDate)) {
            return candidate;
        }

        return candidate.plusYears(1);
    }

    private LocalDate computeLunarYearlyOccurrence(
            Integer lunarDay,
            Integer lunarMonth,
            LocalDate fromDate,
            RepeatType repeatType
    ) {
        try {
            // thử convert sang năm âm tương ứng với năm dương hiện tại
            // năm âm xấp xỉ = năm dương (chỉ lệch 1-2 tháng đầu năm)
            LocalDate candidate = LunarSolarConverter.lunarToSolar(
                    lunarDay, lunarMonth, fromDate.getYear(), false);

            if (!candidate.isBefore(fromDate)) {
                return candidate;
            }

            // đã qua trong năm nay → thử năm sau
            return LunarSolarConverter.lunarToSolar(
                    lunarDay, lunarMonth, fromDate.getYear() + 1, false);

        } catch (Exception e) {
            log.warn("Cannot convert lunar date {}/{} for year {}: {}",
                    lunarDay, lunarMonth, fromDate.getYear(), e.getMessage());
            return null;
        }
    }
}