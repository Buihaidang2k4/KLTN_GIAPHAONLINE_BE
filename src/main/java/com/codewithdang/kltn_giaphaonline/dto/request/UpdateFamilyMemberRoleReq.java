package com.codewithdang.kltn_giaphaonline.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateFamilyMemberRoleReq {
    @NotBlank(message = "Role không được để trống")
    private String roleName;
}
