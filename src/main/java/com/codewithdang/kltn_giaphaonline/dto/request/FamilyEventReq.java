package com.codewithdang.kltn_giaphaonline.dto.request;

import com.codewithdang.kltn_giaphaonline.enums.CalendarType;
import com.codewithdang.kltn_giaphaonline.enums.FamilyEventStatus;
import com.codewithdang.kltn_giaphaonline.enums.ReminderEventType;
import com.codewithdang.kltn_giaphaonline.enums.RepeatType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Event name is required")
    String eventName;
    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime eventTime;
    @NotNull(message = "Day is required")
    @Min(1)
    @Max(31)
    Integer day;
    @NotNull(message = "Month is required")
    @Min(1)
    @Max(12)
    Integer month;
    Integer year;
    FamilyEventStatus status;
    CalendarType calendarType;
    RepeatType repeatType;
    ReminderEventType reminderType;
    String location;
    String locationMapUrl;
    String note;
}
