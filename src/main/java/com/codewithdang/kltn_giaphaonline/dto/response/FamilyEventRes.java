package com.codewithdang.kltn_giaphaonline.dto.response;

import com.codewithdang.kltn_giaphaonline.enums.CalendarType;
import com.codewithdang.kltn_giaphaonline.enums.FamilyEventStatus;
import com.codewithdang.kltn_giaphaonline.enums.ReminderEventType;
import com.codewithdang.kltn_giaphaonline.enums.RepeatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamilyEventRes {
    Long familyEventId;
    Long familyId;
    Long createdByAccountId;
    String eventName;
    LocalTime eventTime;
    Integer day;
    Integer month;
    Integer year;
    LocalDate nextOccurrenceDate;
    FamilyEventStatus status;
    CalendarType calendarType;
    RepeatType repeatType;
    ReminderEventType reminderType;
    String location;
    String locationMapUrl;
    String note;
    Instant createdAt;
    Instant updatedAt;
}
