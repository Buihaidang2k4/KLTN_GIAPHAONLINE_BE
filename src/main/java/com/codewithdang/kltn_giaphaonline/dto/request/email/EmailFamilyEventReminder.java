package com.codewithdang.kltn_giaphaonline.dto.request.email;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailFamilyEventReminder extends EmailBase {
    String recipientName;
    String eventName;
    String eventDate;   // ngày dương lịch đã format
    String familyName;
    String eventUrl;
    boolean isToday;    // true = hôm nay, false = sắp tới
}
