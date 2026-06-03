package com.codewithdang.kltn_giaphaonline.event.consumer.handler;

import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailBase;

public interface EmailHandler<T extends EmailBase> {
    void handle(T email) throws Exception;
}
