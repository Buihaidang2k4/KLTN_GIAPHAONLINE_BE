package com.codewithdang.kltn_giaphaonline.dto.request;

import com.codewithdang.kltn_giaphaonline.enums.AccountStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeStatusLockReq(
        AccountStatus accountStatus,
        @NotNull
        String lockReason
) {
}
