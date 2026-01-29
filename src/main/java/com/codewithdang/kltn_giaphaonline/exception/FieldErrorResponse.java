package com.codewithdang.kltn_giaphaonline.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldErrorResponse {
    private String field;
    private Object rejectedValue;
    private int code;
    private String message;
}

