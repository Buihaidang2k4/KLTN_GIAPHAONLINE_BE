package com.codewithdang.kltn_giaphaonline.dto.request.email;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailOTP extends EmailBase {
    String fullName;
    String otp;
}
