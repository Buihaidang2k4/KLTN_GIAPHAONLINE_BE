package com.codewithdang.kltn_giaphaonline.event.producer;

import com.codewithdang.kltn_giaphaonline.dto.request.email.EmailBase;

public interface EmailHandler<T extends EmailBase> {
    Class<T> supportType();
    void handle(T email) throws Exception;
}
