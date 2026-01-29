package com.codewithdang.kltn_giaphaonline.dto.request;

import com.codewithdang.kltn_giaphaonline.enums.AccountStatus;

public record ChangeStatusLockReq(
        AccountStatus accountStatus,
        String lockReason
) {
}
