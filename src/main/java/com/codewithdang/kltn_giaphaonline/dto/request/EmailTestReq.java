package com.codewithdang.kltn_giaphaonline.dto.request;

public record EmailTestReq(
        String name,
        String toEmail,
        String subject,
        String content
)
{
}
