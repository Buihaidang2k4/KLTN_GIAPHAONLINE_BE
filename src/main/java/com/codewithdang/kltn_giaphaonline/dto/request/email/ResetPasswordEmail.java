package com.codewithdang.kltn_giaphaonline.dto.request.email;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordEmail extends EmailBase {
    String otp;
}
