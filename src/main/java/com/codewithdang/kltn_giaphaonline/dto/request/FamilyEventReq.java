package com.codewithdang.kltn_giaphaonline.dto.request;

import com.codewithdang.kltn_giaphaonline.enums.CalendarType;
import com.codewithdang.kltn_giaphaonline.enums.FamilyEventStatus;
import com.codewithdang.kltn_giaphaonline.enums.ReminderEventType;
import com.codewithdang.kltn_giaphaonline.enums.RepeatType;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime eventTime;
    LocalDate solarDate;
    LocalDate lunarDate;
    FamilyEventStatus status;
    CalendarType calendarType;
    RepeatType repeatType;
    ReminderEventType reminderType;
    String location;
    String locationMapUrl;
    String note;
}
