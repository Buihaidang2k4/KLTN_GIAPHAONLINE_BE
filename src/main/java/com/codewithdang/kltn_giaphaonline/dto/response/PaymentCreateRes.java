package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PaymentHandleRes {
    Long paymentId;
    String paymentUrl;
}
