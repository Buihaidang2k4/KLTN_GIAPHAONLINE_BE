package com.codewithdang.kltn_giaphaonline.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterByInvitationReq(
        @NotBlank(message = "FULL_NAME_REQUIRED")
        String fullName,

        @NotBlank(message = "EMAIL_REQUIRED")
        @Email(message = "EMAIL_INVALID")
        String email,

        String phoneNumber,

        @NotBlank(message = "PASSWORD_REQUIRED")
        String password,

        @NotBlank(message = "CONFIRM_PASSWORD_REQUIRED")
        String confirmPassword,

        @NotBlank(message = "INVITATION_TOKEN_REQUIRED")
        String invitationToken
) {
}