package com.codewithdang.kltn_giaphaonline.dto.request;

import com.codewithdang.kltn_giaphaonline.enums.CalendarType;
import com.codewithdang.kltn_giaphaonline.enums.FamilyEventStatus;
import com.codewithdang.kltn_giaphaonline.enums.ReminderEventType;
import com.codewithdang.kltn_giaphaonline.enums.RepeatType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateFamilyEventReq {
    String eventName;

    LocalTime eventTime;

    LocalDate solarDate;

    LocalDate lunarDate;

    CalendarType calendarType;

    RepeatType repeatType;

    ReminderEventType reminderType;

    FamilyEventStatus familyEventStatus;
    
    String LocationMapUrl;

    String location;


    String note;
}
