package com.codewithdang.kltn_giaphaonline.dto.request;

import com.codewithdang.kltn_giaphaonline.enums.CalendarType;
import com.codewithdang.kltn_giaphaonline.enums.ReminderEventType;
import com.codewithdang.kltn_giaphaonline.enums.RepeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyEventReq {
    String eventName;
    LocalTime eventTime;
    LocalDate solarDate;
    LocalDate lunarDate;
    CalendarType calendarType;
    RepeatType repeatType;
    ReminderEventType reminderType;
    String location;
    String note;
}
