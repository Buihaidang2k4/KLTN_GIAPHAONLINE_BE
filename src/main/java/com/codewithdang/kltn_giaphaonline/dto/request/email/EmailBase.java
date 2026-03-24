package com.codewithdang.kltn_giaphaonline.dto.request.email;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
//@JsonTypeInfo(
//        use = JsonTypeInfo.Id.NAME, property = "type"
//)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = EmailOTP.class, name = "OTP"),
//        @JsonSubTypes.Type(value = EmailWelcome.class, name = "WELCOME")
//})
public abstract class EmailBase {
    String toEmail;
    String subject;
}
