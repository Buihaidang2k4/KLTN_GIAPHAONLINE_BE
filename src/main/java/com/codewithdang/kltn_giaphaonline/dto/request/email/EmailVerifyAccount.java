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
public class EmailVerifyAccount extends EmailBase {
    String fullName;
    String verifyUrl;
}
