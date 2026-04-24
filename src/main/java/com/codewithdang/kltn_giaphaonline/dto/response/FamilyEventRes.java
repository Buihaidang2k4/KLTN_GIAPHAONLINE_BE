package com.codewithdang.kltn_giaphaonline.dto.response;

import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.enums.CalendarType;
import com.codewithdang.kltn_giaphaonline.enums.FamilyEventStatus;
import com.codewithdang.kltn_giaphaonline.enums.ReminderEventType;
import com.codewithdang.kltn_giaphaonline.enums.RepeatType;
import jakarta.persistence.Column;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public class FamilyEventRes {
    Long familyEventId;
    Long familyId;
    Long createdByAccountId;
    String eventName;
    LocalTime eventTime;
    LocalDate solarDate;
    LocalDate lunarDate;
    FamilyEventStatus status;
    CalendarType calendarType;
    RepeatType repeatType;
    ReminderEventType reminderType;
    String location;
    String LocationMapUrl;
    String note;
    Instant createdAt;
    Instant updatedAt;
}
