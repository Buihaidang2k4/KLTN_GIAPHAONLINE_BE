package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FamilySubscriptionCheckQuotaRes {
    Long currentPersonCount;
    Long currentStorageUsedMb;
    Long currentAdminCount;
}
