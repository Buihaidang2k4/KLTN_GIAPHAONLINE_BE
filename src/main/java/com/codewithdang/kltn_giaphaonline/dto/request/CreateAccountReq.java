package com.codewithdang.kltn_giaphaonline.dto.request;


import com.codewithdang.kltn_giaphaonline.enums.RoleEnums;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record CreateAccountReq(
        @NotNull
        RoleEnums roleEnums,

        @NotBlank(message = "Họ tên không được để trống")
        @Size(min = 2, max = 100, message = "Họ tên phải từ 2 đến 100 ký tự")
        String fullName,

        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không đúng định dạng")
        String email,

        @NotBlank(message = "Số điện thoại không được để trống")
        @Pattern(
                regexp = "^(0|\\+84)[0-9]{9,10}$",
                message = "Số điện thoại không hợp lệ"
        )
        String phoneNumber,

        String familyName,

        @NotBlank(message = "Mật khẩu không được để trống")
        @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Mật khẩu phải bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt")
        String password,

        @NotBlank(message = "Vui lòng nhập lại mật khẩu")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Mật khẩu phải bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt")
        String rePassword
) {

    public boolean isPasswordMatching() {
        return password != null && password.equals(rePassword);
    }
}
