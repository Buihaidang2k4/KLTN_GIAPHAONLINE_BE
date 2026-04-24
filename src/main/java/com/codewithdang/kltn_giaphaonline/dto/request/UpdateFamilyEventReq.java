package com.codewithdang.kltn_giaphaonline.dto.request;

import com.codewithdang.kltn_giaphaonline.enums.CalendarType;
import com.codewithdang.kltn_giaphaonline.enums.ReminderEventType;
import com.codewithdang.kltn_giaphaonline.enums.RepeatType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateFamilyEventRequest {
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
