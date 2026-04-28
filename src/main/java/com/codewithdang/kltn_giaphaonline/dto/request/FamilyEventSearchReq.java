package com.codewithdang.kltn_giaphaonline.dto.request;

import com.codewithdang.kltn_giaphaonline.enums.CalendarType;
import com.codewithdang.kltn_giaphaonline.enums.ReminderEventType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class FamilyEventSearchReq {
    @NotNull(message = "FAMILY_ID_IS_REQUIRED")
    Long familyId;

    String keyword;

    CalendarType calendarType;

    LocalDate startDate;

    LocalDate endDate;

    ReminderEventType reminderType;
}