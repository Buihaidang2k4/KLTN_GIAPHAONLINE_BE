package com.codewithdang.kltn_giaphaonline.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterReq(
        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không đúng định dạng")
        String email,

        @NotBlank(message = "Số điện thoại không được để trống")
        @Pattern(
                regexp = "^(0|\\+84)[0-9]{9,10}$",
                message = "Số điện thoại không hợp lệ"
        )
        String phoneNumber,

        @NotBlank(message = "FamilyName không được để trống")
        String familyName,

        @NotBlank(message = "Họ tên không được để trống")
        @Size(min = 2, max = 50, message = "Họ tên từ 2 đến 50 ký tự")
        String fullName,

        @NotBlank(message = "Mật khẩu không được để trống")
        @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
        String password,

        @NotBlank(message = "Xác nhận mật khẩu không được để trống")
        String confirmPassword
) {
}