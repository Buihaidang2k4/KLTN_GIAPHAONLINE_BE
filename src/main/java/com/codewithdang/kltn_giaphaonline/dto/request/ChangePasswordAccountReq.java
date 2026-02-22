package com.codewithdang.kltn_giaphaonline.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordAccountReq(
        @NotBlank(message = "Mật khẩu cũ không được để trống")
        String oldPassword,

        @NotBlank(message = "Mật khẩu mới không được để trống")
        @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Mật khẩu phải bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt")
        String newPassword,

        @NotBlank(message = "Xác nhận mật khẩu không được để trống")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Mật khẩu phải bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt")
        String confirmPassword
) {
}