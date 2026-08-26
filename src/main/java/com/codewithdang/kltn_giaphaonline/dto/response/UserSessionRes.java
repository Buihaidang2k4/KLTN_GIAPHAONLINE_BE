package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionRes implements Serializable {
    private String jwtId;
    private Long accountId;
    private String email;
    private String ipAddress;
    private String userAgent;
    private Instant loginAt;
    private boolean isCurrentSession;
}
