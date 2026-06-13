package com.codewithdang.kltn_giaphaonline.service.family_event;

import com.codewithdang.kltn_giaphaonline.config.fe.FrontendProperties;
import com.codewithdang.kltn_giaphaonline.dto.request.CreateAuditLogReq;
import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailFamilyEventReminder;
import com.codewithdang.kltn_giaphaonline.dto.request.FamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyEventReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyEventRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.FamilyEvent;
import com.codewithdang.kltn_giaphaonline.enums.*;
import com.codewithdang.kltn_giaphaonline.events.producer.EmailProducer;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.FamilyEventMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.FamilyEventRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyMemberRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyRepo;
import com.codewithdang.kltn_giaphaonline.service.audit_log.AuditLogService;
import com.codewithdang.kltn_giaphaonline.service.notification.NotificationService;
import com.codewithdang.kltn_giaphaonline.utils.LunarSolarConverter;
import com.codewithdang.kltn_giaphaonline.utils.SecurityUtils;
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
    AuditLogService auditLogService;
    SecurityUtils securityUtils;
    NotificationService notificationService;
    EmailProducer emailProducer;
    FrontendProperties frontendProperties;
    FamilyMemberRepo familyMemberRepo;

    @Override
    @Transactional
    public FamilyEventRes createEvent(Long familyId, FamilyEventReq req) {
        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));

        Account account = securityUtils.getCurrentAccount();

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

        Account account = securityUtils.getCurrentAccount();
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

        Account account = securityUtils.getCurrentAccount();
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
     * Chạy mỗi ngày lúc 00:01 để cập nhật nextOccurrenceDate cho sự kiện đã qua.
     */
    @Scheduled(cron = "0 1 0 * * ?")
    @Transactional
    public void refreshNextOccurrenceDates() {
        LocalDate today = LocalDate.now();

        List<FamilyEvent> dueEvents =
                familyEventRepo.findAllByNextOccurrenceDateLessThanEqual(today);

        if (dueEvents == null || dueEvents.isEmpty()) return;

        List<FamilyEvent> updatedEvents = new ArrayList<>();

        for (FamilyEvent event : dueEvents) {
            try {
                if (event.getRepeatType() == RepeatType.NONE) {
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
     * Chạy mỗi ngày lúc 07:00:
     * - Gửi thông báo hôm nay nếu nextOccurrenceDate == today
     * - Gửi thông báo nhắc trước nếu hôm nay đúng ngày reminder của sự kiện
     */
    @Scheduled(cron = "0 0 7 * * ?")
    @Transactional(readOnly = true)
    public void sendEventReminders() {
        LocalDate today = LocalDate.now();
        // lấy tất cả event trong 30 ngày tới (bao phủ max MONTH_1)
        List<FamilyEvent> upcomingEvents =
                familyEventRepo.findAllByNextOccurrenceDateBetween(today, today.plusDays(30));

        if (upcomingEvents == null || upcomingEvents.isEmpty()) return;

        for (FamilyEvent event : upcomingEvents) {
            try {
                LocalDate nextDate = event.getNextOccurrenceDate();
                boolean isToday = nextDate.isEqual(today);
                boolean isReminderDay = isReminderDue(event.getReminderType(), nextDate, today);

                if (isToday) {
                    sendEventNotification(event, true);
                } else if (isReminderDay) {
                    sendEventNotification(event, false);
                }
            } catch (Exception ex) {
                log.error("Failed to send reminder for event {}: {}", event.getFamilyEventId(), ex.getMessage(), ex);
            }
        }
    }

    /**
     * Kiểm tra hôm nay có phải ngày nhắc trước theo reminderType không.
     * Ví dụ: DAY_3 → nextDate - 3 ngày == today
     */
    private boolean isReminderDue(ReminderEventType reminderType, LocalDate nextDate, LocalDate today) {
        if (reminderType == null) return false;
        LocalDate reminderDate = switch (reminderType) {
            case DAY_1   -> nextDate.minusDays(1);
            case DAY_3   -> nextDate.minusDays(3);
            case DAY_7   -> nextDate.minusDays(7);
            case DAY_15  -> nextDate.minusDays(15);
            case MONTH_1 -> nextDate.minusMonths(1);
        };
        return reminderDate.isEqual(today);
    }

    private void sendEventNotification(FamilyEvent event, boolean isToday) {
        Long familyId = event.getFamily().getFamilyId();
        String eventDate = event.getNextOccurrenceDate().toString();
        String familyName = event.getFamily().getFamilyName();
        String eventUrl = frontendProperties.getBaseUrl() + "/family/" + familyId + "/events/" + event.getFamilyEventId();
        ReminderEventType reminder = event.getReminderType();

        NotificationType notifType = isToday ? NotificationType.FAMILY_EVENT_TODAY : NotificationType.FAMILY_EVENT_UPCOMING;
        String title = isToday
                ? "Sự kiện hôm nay: " + event.getEventName()
                : "Nhắc nhở: " + event.getEventName() + " - " + (reminder != null ? reminder.getLabel() : "");
        String content = isToday
                ? "Sự kiện '" + event.getEventName() + "' diễn ra hôm nay " + eventDate
                : "Sự kiện '" + event.getEventName() + "' sẽ diễn ra vào ngày " + eventDate
                  + (reminder != null ? " (" + reminder.getLabel() + ")" : "");

        notificationService.createFamilyNotification(
                familyId, null, notifType, title, content,
                event.getFamilyEventId(), "FAMILY_EVENT", eventUrl
        );

        familyMemberRepo.findByFamily_FamilyIdAndStatus(familyId, FamilyMemberStatus.ACTIVE)
                .forEach(member -> emailProducer.sendEmail(
                        EmailFamilyEventReminder.builder()
                                .toEmail(member.getAccount().getEmail())
                                .subject(isToday
                                        ? "[Gia Phả] Sự kiện hôm nay: " + event.getEventName()
                                        : "[Gia Phả] " + (reminder != null ? reminder.getLabel() : "Nhắc nhở") + ": " + event.getEventName())
                                .recipientName(member.getAccount().getFullName())
                                .eventName(event.getEventName())
                                .eventDate(eventDate)
                                .familyName(familyName)
                                .eventUrl(eventUrl)
                                .isToday(isToday)
                                .build()
                ));

        log.info("Sent {} notification for event {} (family {})",
                isToday ? "today" : "reminder-" + reminder, event.getFamilyEventId(), familyId);
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

        if (event.getRepeatType() == RepeatType.YEARLY || event.getRepeatType() == RepeatType.MONTHLY) {
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

        if (day == null) {
            return null;
        }

        RepeatType repeatType = event.getRepeatType() == null ? RepeatType.NONE : event.getRepeatType();

        if (event.getCalendarType() == CalendarType.LUNAR) {
            if (repeatType == RepeatType.MONTHLY) {
                return computeLunarMonthlyOccurrence(day, fromDate);
            }
            return computeLunarYearlyOccurrence(day, month, fromDate, repeatType);
        }

        try {
            if (repeatType == RepeatType.MONTHLY) {
                // tính toán ngày dương lịch
                return computeSolarMonthlyOccurrence(day, fromDate);
            }
            if (month == null) {
                return null;
            }
            if (repeatType == RepeatType.YEARLY) {
                return computeYearlyOccurrence(day, month, fromDate);
            }
            return computeOneTimeOccurrence(day, month, year, fromDate);
        } catch (DateTimeException ex) {
            log.warn(
                    "Invalid event date. eventId={}, day={}, month={}, year={}",
                    event.getFamilyEventId(), day, month, year
            );
            return null;
        }
    }

    /*
    Tính ngày diễn ra tiếp theo của sự kiện dựa vào fromDate.
    Xử lý riêng cho lịch Âm/Dương, loại lặp lại (YEARLY hay NONE).
    Trả về null nếu không còn dịp tiếp theo hoặc dữ liệu không hợp lệ.
     */
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

    /*
    Tính ngày diễn ra cho sự kiện một lần (NONE):
    nếu ngày đó không qua fromDate thì trả về chính ngày đó, nếu đã qua trả null.
     */
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

    /*
    Tính ngày diễn ra cho sự kiện lặp hàng năm (YEARLY) theo lịch Dương: 
    nếu ngày tháng trong năm hiện tại chưa qua fromDate thì dùng năm hiện tại, nếu qua rồi thì dùng năm sau.
     */
    private LocalDate computeLunarYearlyOccurrence(
            Integer lunarDay,
            Integer lunarMonth,
            LocalDate fromDate,
            RepeatType repeatType
    ) {
        try {
            // năm âm xấp xỉ = năm dương
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

    /*
        Tính ngày dương lịch cho lặp lại theo tháng
     */
    private LocalDate computeSolarMonthlyOccurrence(Integer day, LocalDate fromDate) {
        try {
            LocalDate candidate = LocalDate.of(fromDate.getYear(), fromDate.getMonthValue(), day);
            if (!candidate.isBefore(fromDate)) {
                return candidate;
            }
        } catch (DateTimeException ignored) {
            log.error("Ngày không tồn tại : {}", ignored.getMessage());
        }
        LocalDate nextMonth = fromDate.plusMonths(1).withDayOfMonth(1);
        return nextMonth.withDayOfMonth(Math.min(day, nextMonth.lengthOfMonth()));
    }

    /*
        Tính chuyển đổi ngày âm lịch sang dương lịch theo hàng tháng
     */
    private LocalDate computeLunarMonthlyOccurrence(Integer lunarDay, LocalDate fromDate) {
        for (int offset = 0; offset <= 3; offset++) {
            LocalDate probe = fromDate.plusMonths(offset);
            try {
                // lấy tháng âm chính xác từ ngày dương
                int[] lunar = LunarSolarConverter.solarToLunar(probe);
                int lunarMonth = lunar[1];
                int lunarYear = lunar[2];
                LocalDate candidate = LunarSolarConverter.lunarToSolar(lunarDay, lunarMonth, lunarYear, false);
                if (!candidate.isBefore(fromDate)) {
                    return candidate;
                }
            } catch (Exception ignored) {
                log.debug("Invalid lunar date: {}/{}", lunarDay, probe);
            }
        }
        log.warn("Cannot compute lunar monthly occurrence for lunarDay={}", lunarDay);
        return null;
    }


}