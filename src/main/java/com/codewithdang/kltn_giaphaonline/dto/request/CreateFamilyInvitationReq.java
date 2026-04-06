package com.codewithdang.kltn_giaphaonline.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateFamilyInvitationReq {


    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    String invitedEmail;

    @NotBlank(message = "Role không được để trống")
    String roleName;
    String message;
}
